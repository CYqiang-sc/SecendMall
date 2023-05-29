<template>
	<view class="content">
			<SearchBar></SearchBar>
			<view class="_swiper">
				<swiper indicator-dots indicator-active-color="white" autoplay circular>
					<swiper-item v-for="swiper in swiperList" :key="swiper.id">
						<navigator>
							<image :src="apiBaseUrl+'/image/swiper/'+swiper.swiperPic"></image>
						</navigator>
					</swiper-item>
					<!-- <swiper-item>
						<navigator><image src="http://localhost:8082/image/swiper/4.jpg"></image>
						</navigator>
					</swiper-item> -->
				</swiper>
			</view>
			  <view class="index_bigType">
			    <view class="bigTypeRow">
			      <navigator
				  @tap="handleTypeJump"
					:data-index="index"
			      v-for="bigType,index in bigTypeList_row1"
			        :key="bigType.id">
			        <image mode="widthFix" :src="apiBaseUrl+'/image/bigType/'+bigType.image"></image>
			      </navigator>
			    </view>
			    <view class="bigTypeRow">
			      <navigator
				  @tap="handleTypeJump"
					:data-index="index+5"
			      v-for="bigType,index in bigTypeList_row2"
			        :key="bigType.id">
			        <image mode="widthFix" :src="apiBaseUrl+'/image/bigType/'+bigType.image"></image>
			      </navigator>
			    </view>
			  </view>
			
			  <!-- 商品热卖推荐 开始 -->
			  <view class="index_hotProduct">
			    <view class="product_title">热卖推荐</view>
			    <view class="product_list">
			      <view class="product_detail"
			        v-for="hotProduct in hotProductList"
			        :key="hotProduct.id"
			      >
			        <navigator :url="'../product_detail/product_detail?id='+hotProduct.id">
			          <image style="max-height: 290rpx;max-width: 290rpx;" mode="widthFix" :src="apiBaseUrl+'/image/product/'+hotProduct.proPic"></image>
			          <view class="product_name">{{hotProduct.name}}</view>
			          <view class="product_price"> ¥ {{hotProduct.price}}</view>
			          <button size="mini" type="warn">立即购买</button>
			        </navigator>
			      </view>
			    </view>
			  </view>
	</view>
</template>

<script>
	import SearchBar from "@/components/SearchBar.vue";
	import {
		findSwiper,
		getBigType,
		requestUtil,
		getHotProduct,
	} from "../../utils/request.js";
	import {
		apiBaseUrl
	} from "../../utils/config.js";
	export default {
		data() {
			return {
				apiBaseUrl:apiBaseUrl,
				title: 'Hello',
				swiperList:[],
				bigTypeList:[],
				bigTypeList_row1:[],
				bigTypeList_row2:[],
				hotProductList:[]
			}
		},
		onLoad() {
			//获取轮播图
			findSwiper((res) => {
				this.swiperList = res.message;
			}),
			//获取大分类
			// getBigType((res) =>{
			// 		this.bigTypeList=res.message;
			// 	    this.bigTypeList_row1=this.bigTypeList.filter((item,index)=>{
			// 	      return index<5;
			// 	    })
			// 	    this.bigTypeList_row2=this.bigTypeList.filter((item,index)=>{
			// 	      return index>=5;
			// 	    })
			// })
			this.getBigTypeList(),
			getHotProduct((res) =>{
				this.hotProductList= res.message.reverse();
			})
		},
		methods: {
			//获取大分类方法
			async getBigTypeList(){
				const res = await requestUtil({
				  url: '/bigType/findAll',
				  method: "GET"
				});
				this.bigTypeList=res.message;
				this.bigTypeList_row1=this.bigTypeList.filter((item,index)=>{
				  return index<5;
				})
				this.bigTypeList_row2=this.bigTypeList.filter((item,index)=>{
				  return index>=5;
				})
			},
			  // 大类点击事件 跳转 商品分类页面
			  handleTypeJump(e){
			    const {index}=e.currentTarget.dataset;
			  const app=getApp();
			    app.globalData.index=index;
			    uni.switchTab({
			      url: '../category/category'
			    })
			  },
		},
		components:{
			SearchBar,
		}
	}
</script>

<style scoped lang="less">
.content {
	._swiper{
		swiper{
			width: 750rpx;
			height: 375rpx;
			swiper-item{
				image{
					width: 100%;
				}
			}
		}
	}
	
	.index_bigType{
	  padding-top: 20rpx;
	  background-color: #F7F7F7;
	  .bigTypeRow{
		display: flex;
		navigator{
		  flex: 1;
		  image{
			width: 150rpx;
		  }
		}
		navigator:active{
			background-color: #ff5700;
			box-shadow:#ff5700 1rpx 1rpx 1rpx;
		}
	  }
	}
	
	.index_hotProduct{
	  .product_title{
	    font-size: 32rpx;
	    font-weight: 600;
	    padding: 20rpx;
	    color: #ff5700;
	    background-color: #E0E0E0;
	  }
	  .product_list{
	    display: flex;
	    flex-wrap: wrap;
	    .product_detail{
	      margin: 15rpx;
	      width: 46%;
	      text-align: center;
	      navigator{
			box-shadow: #E0E0E0 1rpx 1rpx 5rpx;
	        image{
	          width: 100%;
	          background-color: #F5F5F5;
	        }
	        .product_name{
	          white-space: nowrap;
	          overflow: hidden;
	          text-overflow: ellipsis;
	        }
	        .product_price{
	          color: var(--themeColor);
	        }
	        button{}
	      }
		  navigator:active{
			  box-shadow:var(--themeColor) 1rpx 2rpx 15rpx;
		  }
	    }
	  }
	}
}
</style>
