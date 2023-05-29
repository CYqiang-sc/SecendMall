<template>
	<view>
		<!-- 收货地址 开始 -->
		<view class="recevie_address_row">
			<view class="user_info">
				<view class="user_info_item">{{address.provinceName+address.cityName+address.countyName}}</view>
				<view class="user_info_item user_info_detail">{{address.detailInfo}}</view>
				<text class="user_info_item" :decode="true">{{address.userName}}&nbsp;&nbsp;{{address.telNumber}}</text>
			</view>
		</view>
		<!-- 收货地址 结束 -->

		<!-- 购物车清单 开始 -->
		<view class="cart_content">
			<view class="cart_main">

				<view class="cart_item" v-for="item in cart" :key="item.id">
					<!-- 商品图片 开始 -->
					<navigator class="cart_img_wrap" :url="'/pages/product_detail/product_detail?id='+item.id">
						<image mode="widthFix" :src="apiBaseUrl+'/image/product/'+item.proPic"></image>
					</navigator>
					<!-- 商品图片 结束 -->

					<!-- 商品信息 开始 -->
					<view class="cart_info_wrap">
						<navigator :url="'/pages/product_detail/index?id='+item.id">
							<view class="goods_name">{{item.name}}</view>
						</navigator>
						<view class="goods_price_wrap">
							<view class="goods_price">
								¥{{item.price}}
							</view>
							<view class="cart_num_tool">

								<view class="goods_num">×{{item.num}}</view>

							</view>
						</view>
					</view>
					<!-- 商品信息 结束 -->
				</view>

			</view>
		</view>
		<!-- 购物车清单 结束 -->

		<!-- 底部工具栏 开始 -->
		<view class="footer_tool">


			<!-- 合计 开始 -->
			<view class="total_price_wrap">
				<view class="total_price">
					共{{totalNum}}件，合计<text class="total_price_text" :decode="true">&nbsp;¥ {{totalPrice}}</text>
				</view>
			</view>
			<!-- 合计 结束 -->

			<!-- 结算 开始 -->
			<view class="order_pay_wrap" @tap="handleOrderPay">
				去付款
			</view>
			<!-- 结算 结束 -->
		</view>
		<!-- 底部工具栏 结束 -->
	</view>
</template>

<script>
	import {
		getWxLogin,
		getUserProfile,
		requestPay,
		requestUtil
	} from '../../utils/request.js';
	import {
		apiBaseUrl
	} from "../../utils/config.js";
	export default {
		data() {
			return {
				address: {},
				apiBaseUrl: apiBaseUrl,
				cart: [],
				totalPrice: 0,
				totalNum: 0
			};
		},
		onShow() {
			const address = uni.getStorageSync('address');
			let cart = uni.getStorageSync('cart') || [];
			cart = cart.filter(v => v.checked);
			let totalPrice = 0;
			let totalNum = 0;
			cart.forEach(v => {
				totalPrice += v.price * v.num;
				totalNum += v.num;
			})
			this.cart = cart;
			this.totalNum = totalNum;
			this.address = address;
			this.totalPrice = totalPrice;
		},
		methods: {
			// 处理订单支付
			handleOrderPay() {
				// wx.login({
				//   timeout: 5000,
				//   success:(res)=>{
				//     console.log(res.code)
				//   }
				// })
				// let res=await getWxLogin();
				// console.log(res.code)

				// wx.getUserProfile({
				//   desc: '获取用户信息',
				//   success:(res)=>{
				//     console.log(res.userInfo.nickName,res.userInfo.avatarUrl)
				//   }
				// })

				// let res2=await getUserProfile();
				// console.log(res2.userInfo.nickName,res2.userInfo.avatarUrl)

				const token = uni.getStorageSync('token');
				if (!token) {
					Promise.all([getWxLogin(), getUserProfile()]).then((res) => {
						let loginParam = {
							code: res[0].code,
							nickName: res[1].userInfo.nickName,
							avatarUrl: res[1].userInfo.avatarUrl
						}
						uni.setStorageSync('userInfo', res[1].userInfo);
						this.wxlogin(loginParam);
					})
				} else {
					// 支付继续走，创建订单
					console.log("支付继续走，创建订单");
					this.createOrder();
				}
				//    Promise.all([getWxLogin(),getUserProfile()]).then((res)=>{
				// let loginParam={
				// 	code:res[0].code,
				// 	nickName:res[1].userInfo.nickName,
				// 	avatarUrl:res[1].userInfo.avatarUrl
				//   }
				//   console.log(loginParam)
				//   uni.setStorageSync('userInfo', res[1].userInfo);
				//   this.wxlogin(loginParam);
				//    })
			},
			/**
			 * 请求后端获取用户token
			 * @param {*} loginParam 
			 */
			async wxlogin(loginParam) {
				const result = await requestUtil({
					url: "/user/wxlogin",
					data: loginParam,
					method: "post"
				});
				console.log('logpa' + result);
				const token = result.token;
				if (result.code === 200) {
					// 存储token到缓存
					uni.setStorageSync('token', token);
					// 支付继续走，创建订单
					console.log("支付继续走，创建订单");
					this.createOrder();
				}
			},
			async createOrder() {
				const totalPrice = this.$data.totalPrice;
				const address = this.$data.address.provinceName + this.$data.address.cityName + this.$data.address
					.countyName + this.$data.address.detailInfo;
				const consignee = this.$data.address.userName;
				const telNumber = this.$data.address.telNumber;
				let goods = [];
				this.$data.cart.forEach(v => goods.push({
					goodsId: v.id,
					goodsNumber: v.num,
					goodsPrice: v.price,
					goodsName: v.name,
					goodsPic: v.proPic
				}))
				const orderParam = {
					totalPrice,
					address,
					consignee,
					telNumber,
					goods
				}
				const res = await requestUtil({
					url: "/my/order/create",
					method: "POST",
					data: orderParam
				});
				let orderNo = res.orderNo;
				// 调用统一下单，预支付
				const preparePayRes = await requestUtil({
					url: "/my/order/preparePay",
					method: "POST",
					data: orderNo
				});
				let payRes=await requestPay(preparePayRes).then(()=>{},()=>{
					console.log(orderNo)
					uni.showModal({
						content:'确认支付',
						success: (res) => {
							if(res.confirm){
								requestUtil({
									url: "/my/order/successPay",
									method: "POST",
									data: orderNo
								}).then(()=>{
								  // 删除缓冲中，已经支付的商品
								  let newCart=wx.getStorageSync('cart');
								  newCart=newCart.filter(v=>!v.checked);
								  this.cart = [];
								  this.totalPrice = 0;
								  this.totalNum = 0;
								  uni.setStorageSync('cart', newCart);
								  uni.showToast({
									title: '支付成功',
									icon:'none'
								  });
								  uni.navigateTo({
								  	url:'/pages/order/order?type=0'
								  });
								});
							}else if(res.cancel){
								console.log('取消支付')
							}
						}
					})
				})
			}
		}
	}
</script>

<style lang="less">
	page {
		padding-bottom: 70rpx;
	}

	.recevie_address_row {
		border-bottom: 1px dotted gray;
		padding: 20rpx;

		.user_info {
			.user_info_item {
				margin-top: 10rpx;
			}

			.user_info_detail {
				font-size: 20px;
				font-weight: bolder;
				margin-bottom: 10rpx;
			}
		}

	}


	.cart_content {
		background-color: #F5F5F5;

		.cart_main {
			padding: 2rpx 10rpx 10rpx 10rpx;

			.cart_item {
				display: flex;
				background-color: white;
				border-radius: 10px;
				margin: 20rpx;
				padding-right: 20rpx;
				height: 200rpx;

				.cart_img_wrap {
					flex: 2;
					display: flex;
					justify-content: center;
					align-items: center;
					background-color: #F5F5F5;
					margin: 20rpx;
					border-radius: 10px;

					image {
						width: 80%;
						height: 100%;
					}
				}

				.cart_info_wrap {
					flex: 4;
					display: flex;
					flex-direction: column;
					justify-content: space-around;

					.goods_name {
						font-weight: bolder;
						display: -webkit-box;
						overflow: hidden;
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 2;
					}

					.goods_price_wrap {
						display: flex;
						justify-content: space-between;

						.goods_price {
							color: var(--themeColor);
							font-size: 34rpx;
						}

						.cart_num_tool {
							display: flex;

							.goods_num {
								display: flex;
								justify-content: center;
								align-items: center;

							}
						}
					}
				}
			}
		}
	}


	.footer_tool {
		display: flex;
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		height: 90rpx;
		background-color: #fff;
		border-top: 1px solid #ccc;
		padding-left: 30rpx;

		.total_price_wrap {
			flex: 5;
			display: flex;

			align-items: center;

			.total_price {
				.total_price_text {
					color: var(--themeColor);
					font-weight: bolder;
					font-size: 34rpx;
				}
			}
		}

		.order_pay_wrap {
			flex: 3;
			display: flex;
			justify-content: center;
			align-items: center;
			background-image: linear-gradient(90deg, #FF740B, #FE6227);
			color: #fff;
			font-size: 32rpx;
			font-weight: 600;
			border-radius: 20px;
			margin: 10rpx;
		}
	}
</style>
