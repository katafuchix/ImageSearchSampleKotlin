package com.example.imagesearchsample.ui

import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.imagesearchsample.R
import com.example.imagesearchsample.databinding.FragmentMainBinding
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchsample.extensions.autoCleared
import com.example.imagesearchsample.viewmodel.ImageSearchAdapter
import com.example.imagesearchsample.viewmodel.ImageSearchAdapterViewModel
import com.example.imagesearchsample.viewmodel.SearchViewModel
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.view_image_slide.view.*


class MainFragment : Fragment() {

    private var binding by autoCleared<FragmentMainBinding>()
    private val viewModel
            by lazy {
                context?.run {
                    ViewModelProviders.of(this@MainFragment, SearchViewModel.Factory())
                        .get(SearchViewModel::class.java)
                }
            }

    private val adapter by lazy {
        ImageSearchAdapter().apply {
            this.lifecycleOwner = this@MainFragment
            this.listener = object : ImageSearchAdapter.OnClickListener {
                override fun onClick(item: ImageSearchAdapterViewModel) {
                    onClickImage(item)
                }
            }
        }
    }

    var hud: KProgressHUD? = null
    var isLoading = MutableLiveData<Boolean>()
    var imageIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
                .apply{
                    val binding = this
                    val mainFragment = this@MainFragment

                    binding.lifecycleOwner = mainFragment
                    binding.viewModel = mainFragment.viewModel
                    binding.presenter = mainFragment

                    binding.listRecyclerView.apply {
                        this.layoutManager = GridLayoutManager(mainFragment.context, 3)
                        this.setHasFixedSize(true)
                    }

                    // 検索窓
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextChange(newText: String): Boolean {
                            return false
                        }

                        // 検索ワード決定時
                        override fun onQueryTextSubmit(query: String): Boolean {
                            // ロード中
                            isLoading.value = true
                            // リストをクリア
                            adapter.dataList.clear()
                            // キーボードを下げる
                            searchView.clearFocus()
                            // 検索
                            mainFragment.viewModel?.getImages(query)
                            return true
                        }
                    })

                    // 検索結果
                    mainFragment.viewModel?.imagesResult?.observe(mainFragment, Observer {
                        // リストデータ追加
                        adapter.dataList.addAll(
                            it.imagesData.map {
                                ImageSearchAdapterViewModel().apply {
                                    val viewModel = this
                                    viewModel.url.value = it
                                }
                            }
                        )
                        // 検索終了
                        this@MainFragment.isLoading.value = false
                    })

                    // ロード中
                    isLoading.observe(mainFragment, Observer {
                        if (it) {
                            hud = KProgressHUD.create(this@MainFragment.context)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            hud!!.show()
                        } else {
                            hud?.dismiss()
                        }
                    })
                }

        // UI設定
        return binding.root.apply {

            val root = this
            this.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // RecyclerView に adapter を設定
                    binding.listRecyclerView.adapter = this@MainFragment.adapter.apply {
                        this.screenWidth = root.width
                    }
                }
            })
        }
    }

    fun onClickImage(item: ImageSearchAdapterViewModel) {
        binding.viewModel?.imagesResult?.value.apply {
           imageIndex = this?.imagesData?.indexOf(item.url.value)!!
            showImageSlides()
        }
    }

    // プロフィール画像スライド表示
    fun showImageSlides() {

        val root = this
        val inflater = LayoutInflater.from(activity!!.applicationContext)
        val imageSlide = inflater.inflate(R.layout.view_image_slide, null)
        imageSlide.viewPager.setHasFixedSize(true)

        // 全画面サイズでポップアップ
        val popupWindow = PopupWindow(
            imageSlide,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        // バージョン考慮
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 表示時のアニメーション
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.BOTTOM
            popupWindow.enterTransition = slideIn

            // 閉じる時のアニメーション
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.BOTTOM
            popupWindow.exitTransition = slideOut
        }

        // 閉じるボタン
        imageSlide.closeButton.setOnClickListener {
            popupWindow.dismiss()
        }

        imageSlide.viewPager.layoutManager =
            LinearLayoutManager(this@MainFragment.context).apply {
                this.orientation = RecyclerView.HORIZONTAL
                // TODO it.membersData.offersData.subImage
                scrollToPositionWithOffset(root.imageIndex, 3);
                // ページング用オブジェクトを設定
                PagerSnapHelper().attachToRecyclerView(imageSlide.viewPager)
            }

        imageSlide.viewPager.adapter = ImagesSlideAdapter().apply {
            val adapter = this
            this.screenWidth = root.view?.width
            viewModel?.imagesResult.apply {
                this?.value?.imagesData?.forEach { url ->
                    adapter.itemList.addItem(ImagesSlideAdapter.Item.Data(url))
                }
            }
            this.itemList.isSelected(root.imageIndex)
        }
        // 表示
        TransitionManager.beginDelayedTransition(binding.frameLayoutFragment)
        popupWindow.showAtLocation(binding.frameLayoutFragment, Gravity.CENTER, 0, 0)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }

        val TAG: String = MainFragment::class.java.simpleName
    }
}
