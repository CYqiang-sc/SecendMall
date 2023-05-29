<template>
	<view style="background-color: #F5F5F5;">
		<!-- 收货地址 -->
		<view class="recevie_address_row">
			<view class="address_btn" v-if="!address">
				<button type="warn" plain @tap="handleChooseAddress">获取收货地址</button>
			</view>
			<view v-else class="user_info_row">
				<view class="user_info">
					<view>收货人：{{address.userName}},{{address.telNumber}}</view>
					<view>{{address.provinceName+address.cityName+address.countyName+address.detailInfo}}</view>
				</view>

				<view class="change_address_btn">
					<button size="mini" plain>更换地址</button>
				</view>
			</view>
		</view>
		
		<!-- 购物车清单 -->
		<view class="cart_content">
		  <view class="cart_main">
		    <view class="cart_item"
		      v-for="item in cart"
		      :key="item.id"
		    >
		      <!-- 复选框 -->
		      <view class="cart_chk_wrap">
		        <checkbox-group :data-id="item.id" @change="handleItemChange">
		          <checkbox :checked="item.checked"></checkbox>
		        </checkbox-group>
		      </view>
		
		      <!-- 商品图片 -->
		      <navigator class="cart_img_wrap" :url="'/pages/product_detail/product_detail?id='+item.id">
		        <image mode="widthFix" :src="apiBaseUrl+'/image/product/'+item.proPic"></image>
		      </navigator>
		
		      <!-- 商品信息 -->
		      <view class="cart_info_wrap">
		        <navigator :url="'/pages/product_detail/product_detail?id='+item.id">
		          <view class="goods_name">{{item.name}}</view>
		        </navigator>
		        <view class="goods_price_wrap">
		          <view class="goods_price">
		            ¥{{item.price}}
		          </view>
		          <view class="cart_num_tool">
		            <view class="num_edit" @tap="handleItemNumEdit" :data-id="item.id" :data-operation="-1">-</view>
		            <view class="goods_num">{{item.num}}</view>
		            <view class="num_edit" @tap="handleItemNumEdit" :data-id="item.id" :data-operation="1">+</view>
		          </view>
		        </view>
		      </view>
		    </view>
		  </view>
		</view>
		<block v-if = "cart.length==0">
		  <view class="iconfont icon-gouwuchekong" style="font-size: 200rpx;color: gray;text-align: center;padding-top: 100rpx;"></view>
		  <view style="text-align: center;color: gray;">购物车空空如也~</view>
		</block>
		<view  style="height: 60vh;"></view>
		<!-- 购物车清单 -->
		
		<!-- 底部工具栏 -->
		<view class="footer_tool">
		  <!-- 全选 -->
		  <view class="all_chk_wrap">
		    <checkbox-group @change="handleItemAllCheck">
		      <checkbox :checked="allChecked"><text decode="true">&nbsp;&nbsp;全选</text></checkbox>
		    </checkbox-group>
		  </view>
		
		  <!-- 合计 -->
		  <view class="total_price_wrap">
		    <view class="total_price">
		      合计：<text class="total_price_text">¥ {{totalPrice}}</text>
		    </view>
		  </view>
		
		  <!-- 结算 -->
		  <view class="order_pay_wrap" @tap="handlePay">
		    结算({{totalNum}})
		  </view>
		</view>
		<!-- 底部工具栏 结束 -->
	</view>
</template>

<script>
	import {
		apiBaseUrl
	} from "../../utils/config.js";
	export default {
		data() {
			return {
				address: {},
				apiBaseUrl:apiBaseUrl,
				cart:[],
				allChecked:false,
				totalPrice:0,
				totalNum:0
			};
		},
		methods:{
			handleChooseAddress() {
				uni.chooseAddress({
					success: (result) => {
						console.log(result,'123456')
						uni.setStorageSync('address', result)
					},
					fail: (rej) => {
						console.log(rej,'123')
					}
				})
			},
			  // 商品选中事件处理
			  handleItemChange(e){
			    const {id}=e.currentTarget.dataset;
			    let cart=this.cart;
			    let index=cart.findIndex(v=>v.id===id);
			    cart[index].checked=!cart[index].checked;
			
			    this.setCart(cart);
			  },
			  // 商品全选事件处理
			    handleItemAllCheck(){
			      let {cart,allChecked}=this.$data;
			      console.log(cart,allChecked)
			      allChecked=!allChecked;
			      cart.forEach(v=>v.checked=allChecked);
			      this.setCart(cart);
			    },
			 // 商品数量的编辑功能
			  handleItemNumEdit(e){
				const {id,operation}=e.currentTarget.dataset;
				console.log(id,operation)
				let {cart}=this.$data;
				let index=cart.findIndex(v=>v.id===id);
				if(cart[index].num===1 && operation === -1){
				  wx.showModal({
					title:'温馨提示',
					content:'您是否要删除该商品？',
					cancelColor: 'cancelColor',
					success:(res)=>{
					  if(res.confirm){
						cart.splice(index,1);
						this.setCart(cart);
					  }
					}
				  })
				}else{
				  cart[index].num+=operation;
				  this.setCart(cart);
				}
			  },
			  // 点击结算
			    handlePay(){
			      const {address,totalNum}=this.$data;
			      if(!address){
			        uni.showToast({
			          title: '您还没有选择收货地址',
			          icon:'none'
			        })
			        return;
			      }
			      if(totalNum===0){
			        uni.showToast({
			          title: '您还没有选购商品',
			          icon:'none'
			        })
			        return;
			      }
			      uni.navigateTo({
			        url: '/pages/pay/pay'
			      })
			    },
			//计算购物车
			setCart(cart){
			    let allChecked=true;
			    let totalPrice=0;
			    let totalNum=0;
			    cart.forEach(v=>{
			      if(v.checked){
			        totalPrice+=v.price*v.num;
			        totalNum+=v.num;
			      }else{
			        allChecked=false;
			      }
			    })
			    allChecked=cart.length!=0?allChecked:false;
				this.cart = cart
				this.allChecked = allChecked
				this.totalPrice = totalPrice
				this.totalNum = totalNum
			    // cart设置到缓存中
			    wx.setStorageSync('cart', cart);
			}
		},
		onShow: function() {
			const address = uni.getStorageSync('address');
			this.address = address;
			const cart = uni.getStorageSync('cart')||[];
			this.setCart(cart);
			// console.log(address,'dadadad')
		},
	}
</script>

<style lang="less">
	.recevie_address_row {
		.address_btn {
			padding: 10rpx;
			button {
				width: 60%;
			}
		}

		.user_info_row {
			display: flex;
			padding: 20rpx;

			.user_info {
				flex: 5;
			}

			.change_address_btn {
				flex: 3;
				text-align: right;

				button {
					border: 1px solid gray;
					font-weight: normal;
				}
			}
		}
	}
	
	.cart_content{
	  background-color: #F5F5F5;
	  .cart_main{
	    padding: 2rpx 10rpx 10rpx 10rpx;
	    .cart_item{
	      display: flex;
	      background-color: white;
	      border-radius: 10px;
	      margin: 20rpx;
	      padding-right: 20rpx;
	      .cart_chk_wrap{
	        flex:1;
	        display: flex;
	        justify-content: center;
	        align-items: center;
	        margin: 20rpx;
	      }
	      .cart_img_wrap{
	        flex:2;
	        display: flex;
	        justify-content: center;
	        align-items: center;
	        background-color: #F5F5F5;
	        margin: 20rpx;
	        border-radius: 10px;
	        image{
	          width: 80%;
	        }
	      }
	      .cart_info_wrap{
	        flex:4;
	        display: flex;
	        flex-direction: column;
	        justify-content: space-around;
	        .goods_name{
	          font-weight: bolder;
	          display: -webkit-box;
	          overflow: hidden;
	          -webkit-box-orient: vertical;
	          -webkit-line-clamp: 2;
	        }
	        .goods_price_wrap{
	          display: flex;
	          justify-content: space-between;
	          .goods_price{
	            color: var(--themeColor);
	            font-size: 34rpx;
	          }
	          .cart_num_tool{
	            display: flex;
	            .num_edit{
	              display: flex;
	              justify-content: center;
	              align-items: center;
	              width: 55rpx;
	              height: 55rpx;
	            }
	            .goods_num{
	              display: flex;
	              justify-content: center;
	              align-items: center;
	              width: 85rpx;
	              height: 55rpx;
	              background-color: #F5F5F5;
	            }
	          }
	        }
	      }
	    }
	  }
	}
	
	.footer_tool{
	  display: flex;
	  position: fixed;
	  bottom: 92rpx;
	  left: 0;
	  width: 100%;
	  height: 92rpx;
	  background-color: #fff;
	  border-top: 1px solid #ccc;
	  .all_chk_wrap{
	    flex:2;
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    padding-left: 25rpx;
	  }
	  .total_price_wrap{
	    flex:5;
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    .total_price{
	      .total_price_text{
	        color: var(--themeColor);
	        font-weight: bolder;
	        font-size: 34rpx;
	      }
	    }
	  }
	  .order_pay_wrap{
	    flex:3;
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    background-image: linear-gradient(90deg,#FF740B,#FE6227);
	    color:#fff;
	    font-size: 32rpx;
	    font-weight: 600;
	    border-radius: 20px;
	    margin: 10rpx;
	  }
	}
</style>
