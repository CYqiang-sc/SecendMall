<template>
	<view class="mypage">
		<view class="user_info">
			<!-- 用户背景信息开始 -->
			<view class="user_info_bg">
				<view class="user_info_wrap">
					<image :src="userInfo.avatarUrl"></image>
					<view class="user_name">{{userInfo.nickName}}</view>
				</view>
			</view>
			<!-- 用户背景信息结束 -->

			<!-- 用户操作菜单开始 -->
			<view class="user_menu">

				<!-- 订单管理开始 -->
				<view class="order_wrap">
					<view class="order_title">我的订单</view>
					<view class="order_content">
						<navigator url="/pages/order/order?type=1">
							<view class="iconfont icon-daifukuan"></view>
							<view class="order_name">待付款</view>
						</navigator>
						<navigator url="/pages/order/order?type=2">
							<view class="iconfont icon-fonts-daishouhuo"></view>
							<view class="order_name">待收货</view>
						</navigator>
						<navigator url="/pages/order/order?type=3">
							<view class="iconfont icon-tuikuan"></view>
							<view class="order_name">退款/退货</view>
						</navigator>
						<navigator url="/pages/order/order?type=0">
							<view class="iconfont icon-dingdan"></view>
							<view class="order_name">全部订单</view>
						</navigator>
						<navigator url="/pages/audit/audit">
							<view class="iconfont icon-shangpin"></view>
							<view class="order_name">我的商品</view>
						</navigator>
					</view>
				</view>
				<!-- 订单管理结束 -->

				<!-- 收货地址管理开始 -->
				<view class="address_wrap" @tap="handleEditAddress">
					收货地址管理
				</view>
				<!-- 收货地址管理结束 -->

				<!-- 应用相关信息开始 -->
				<view class="app_info_wrap">
					<view class="app_info_item app_info_contact">
						<text>联系客服</text>
						<text>400-699-12345</text>
					</view>
					<view class="app_info_item">
						<button open-type="feedback" class="feedback">意见反馈</button>
					</view>
					<view class="app_info_item">
						关于我们
					</view>
				</view>
				<!-- 应用相关信息结束 -->

			</view>
			<!-- 用户操作菜单结束 -->
		</view>
	</view>
</template>

<script>
	import {
		requestUtil,
		getWxLogin,
		getUserProfile,
	} from '../../utils/request.js';
	import {
		apiBaseUrl
	} from "../../utils/config.js";
	export default {
		data() {
			return {
				userInfo: {}
			}
		},
		onLoad: function(options) {
			const token = uni.getStorageSync('token');
			if (!token) {
				wx.showModal({
					title: '友情提示',
					content: '微信授权登录后，才可进入个人中心',
					success: (res) => {
						Promise.all([getWxLogin(), getUserProfile()]).then((res) => {
							console.log(res[0].code);
							console.log(res[1].userInfo.nickName, res[1].userInfo.avatarUrl)
							let loginParam = {
								code: res[0].code,
								nickName: res[1].userInfo.nickName,
								avatarUrl: res[1].userInfo.avatarUrl
							}
							console.log(loginParam)
							uni.setStorageSync('userInfo', res[1].userInfo);
							this.wxlogin(loginParam);
							this.userInfo = res[1].userInfo;
						})
					}
				})
			} else {
				console.log("token存在：" + token);
				const userInfo = uni.getStorageSync('userInfo')
				this.userInfo = userInfo
			}
		},
		methods: {
			onChooseAvatar(e) {
				this.uploadFilePromise(e.detail.avatarUrl)
			},
			onNickName(e) {
				this.completeMemberInfo(e.detail.value)
			},
			uploadFilePromise(avatarUrl) {
				//调后台接口上传图片  这里的avatarUrl拿到的是微信地址 
				uni.request({
					url: 'https://example.weixin.qq.com/upload',
					data: avatarUrl,
					success(res) {
						const data = res.data
						console.log(res)
						//do something  
						completeMemberInfo(res.data.avatarUrl)
					}
				})
			},
			//接收 avatarUrl / nickName 
			completeMemberInfo(data) {
				//接收 avatarUrl / nickName 调接口完善头像或者昵称信息

			},
			
			  // 点击 编辑收货地址
			  handleEditAddress(){
			    uni.chooseAddress({
			      success: (result) => {},
			    })
			  },
			async wxlogin(loginParam) {
				const result = await requestUtil({
					url: "/user/wxlogin",
					data: loginParam,
					method: "post"
				});
				console.log(result);
				const token = result.token;
				if (result.code === 200) {
					// 存储token到缓存
					uni.setStorageSync('token', token);
					uni.setStorageSync('userId', result.userId);
				}
			},


		}
	}
</script>

<style lang="scss">
	.mypage {
		background-color: #F6F6F4;
	}


	.user_info {
		.user_info_bg {
			position: relative;
			height: 40vh;
			background-color: $uni-bg-color;

			.user_info_wrap {
				position: relative;
				top: 30%;
				text-align: center;

				image {
					width: 150rpx;
					height: 150rpx;
					border-radius: 50%;
				}

				.user_name {
					font-weight: 700;
					color: white;
				}
			}
		}

		
		  .user_menu{
		    margin-top: 15rpx;
		    background-color: #F6F6F4;
		    .order_wrap{
		      margin: 15rpx;
		      background-color: #fff;
		      .order_title{
		        padding: 15rpx;
		        padding-left: 35rpx;
		        border-bottom: 5rpx solid #F6F6F4;
		      }
		      .order_content{
		        padding: 20rpx;
		        display: flex;
		        text-align: center;
		        navigator{
		          flex:1;
		          padding: 15rpx 0;
		          .iconfont{
		            font-size: 24px;
		          }
		          .order_name{
		
		          }
		        }
		      }
		    }
		
		    .address_wrap{
		      margin: 15rpx;
		      margin-top: 20rpx;
		      background-color: #fff;
		      padding: 20rpx 0;
		      padding-left: 35rpx;
		    }
		
		    .app_info_wrap{
		      margin: 15rpx;
		      margin-top: 20rpx;
		      background-color: #fff;
		      .app_info_item{
		        padding: 20rpx;
		        padding-left: 35rpx;
		        border-bottom: 5rpx solid #F6F6F4;
		      }
		      .app_info_contact{
		        display: flex;
		        justify-content: space-between;
		      }
		      .feedback{
		        margin: 0;
		        padding: 0;
		        background-color: transparent;
		        width: 100%;
		        height: 100%;
		        display: flex;
		        justify-content: start;
		        align-items: center;
		        font-size: 28rpx;
		        font-weight: normal;
		        flex-grow: 1;
		      }
		      .feedback::after {
		        border: none;
		      }
		
		    }
		  }
	}
</style>
