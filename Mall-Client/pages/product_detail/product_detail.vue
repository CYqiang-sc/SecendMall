<template>
	<view>
		<view class="product_swiper" v-if="productObj.productSwiperImageList!=0">
		  <swiper autoplay circular	indicator-dots>
			  <swiper-item 
				v-for="product in productObj.productSwiperImageList"
				:key="product.id"
			  >
				<navigator>
				  <image mode="widthFix" :src="apiBaseUrl+'/image/productSwiperImgs/'+product.image"></image>
				</navigator>
			  </swiper-item>
			</swiper>
		</view>
		
		<view class="product_price"> ¥ {{productObj.price}}</view>
		<view class="product_name">{{productObj.name}}</view>
		<view class="product_descripion">{{productObj.description}}</view>
		
		<view class="desc_tabs">
		  <view class="tabs_title">
		    <view :class="['title_item',activeIndex==0?'active':'']" data-index="0" @tap="handleItemTap">商品介绍</view>
		    <view :class="['title_item',activeIndex==1?'active':'']" data-index="1"  @tap="handleItemTap">规格参数</view>
		  </view>
		
		  <view class="tabs_content">
		    <block v-if="activeIndex==0">
		      <rich-text :nodes="productObj.productIntroImgs"></rich-text>
		    </block>
		    <block v-else-if="activeIndex==1">
		      <rich-text :nodes="productObj.productParaImgs"></rich-text>
		    </block>
		  </view>
		</view>
		
		
		<view class="btm_tool">
		  <navigator open-type="switchTab" url="/pages/index/index" class="tool_item">
		    <view class="iconfont icon-home"></view>
		    <view>首页</view>
		  </navigator>
		
		  <view class="tool_item">
		    <view class="iconfont icon-kefu"></view>
		    <view>客服</view>
		    <button open-type="contact"></button>
		  </view>
		
		  <navigator open-type="switchTab" url="/pages/cart/cart" class="tool_item">
		    <view class="iconfont icon-gouwuche"></view>
		    <view>购物车</view>
		  </navigator>
		
		  <view class="tool_item btn_cart" @tap="handleCartAdd">
		    <view>加入购物车</view>
		  </view>
		
		  <view class="tool_item btn_buy" @tap="handleBuy">
		    <view>立即购买</view>
		  </view>
		</view>
	</view>
	
</template>

<script>
	import {
		requestUtil,
		getProductDetail
	} from "../../utils/request.js";
	import {
		apiBaseUrl
	} from "../../utils/config.js";
	export default {
		data() {
			return {
				apiBaseUrl:apiBaseUrl,
				productObj:{},
				activeIndex:0
			};
		},
		productInfo:{

		},
		onLoad(options) {
			getProductDetail((res) =>{
				this.productObj = res.message
				console.log("3333",this.productObj)
				this.productInfo=res.message;
				},options.id)
		},
		methods:{
			handleItemTap(e){
			    const {index}=e.currentTarget.dataset;
			    this.activeIndex = index
			  },
			// 点击事件 商品加入购物车
			handleCartAdd(){
				this.setCartadd();
				uni.showToast({
					title: '加入成功',
					icon:'success',
					mask:true
				})
			},
			// 加入购物车
			setCartadd(){
				let cart=uni.getStorageSync('cart')||[];
				let index=cart.findIndex(v=>v.id===this.productInfo.id);
				if(index===-1){ // 购物车里面不存在当前商品 
					this.productInfo.num=1;
					this.productInfo.checked=true;
					cart.push(this.productInfo);
				}else{ // 已经存在
					cart[index].num++;
				}
				uni.setStorageSync('cart', cart); // 把购物车添加到缓存中
			}, 
			  // 点击 立即购买
			  handleBuy(){
				this.setCartadd();
				wx.switchTab({
				  url: '/pages/cart/cart',
				})
			  },

		}
	}
</script>

<style lang="less">
.product_swiper{
  swiper{
    height: 100vw;
    swiper-item{
      navigator{
        image{
			width: 100%;
		}
      }
    }
  }
}

.product_price{
  padding: 15rpx;
  font-size: 42rpx;
  font-weight: 600;
  color: var(--themeColor);
}

.product_name{
  padding: 15rpx;
  font-size: 40rpx;
  font-weight: 600;
  border-top: 1rpx solid #dedede;
}

.product_descripion{
  padding: 0 10rpx 25rpx 10rpx;
  border-bottom: 1rpx solid #dedede;
}

.desc_tabs{
  .tabs_title{
    display: flex;
    .title_item{
      flex:1;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 15rpx;
    }
    .active{
      color: var(--themeColor);
    }
  }
  .tabs_content{
    rich-text{

    }
  }
}

.btm_tool{
  padding: 10rpx;
  position: fixed;
  left: 0;
  bottom: 0;
  display: flex;
  width: 100%;
  background-color: #fff;
  height: 90rpx;
  .tool_item{
    flex:1;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    font-size: 24rpx;
    position: relative;
    button{
      position: absolute;
      top:0;
      left:0;
      width: 100%;
      height: 100%;
      opacity: 0;
    }
  }

  .btn_cart{
    flex:2;
    justify-content: center;
    align-items: center;
    font-size: 30rpx;
    font-weight: 600;
    background-image: linear-gradient(90deg,#fdcf00,#fd9b00);
    color: #fff;
    border-bottom-left-radius: 50rpx;
    border-top-left-radius: 50rpx;
  }

  .btn_buy{
    flex:2;
    justify-content: center;
    align-items: center;
    font-size: 30rpx;
    font-weight: 600;
    background-image: linear-gradient(90deg,#ff7310,#fe3f00);
    color: #fff;
    border-bottom-right-radius: 50rpx;
    border-top-right-radius: 50rpx;
  }
}
</style>
