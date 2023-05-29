<template>
	<view>
		<view class="head-nav">
			<view style="margin: 0 auto;;color: #FE6227;">
				商品审核
			</view>
		</view>
			<view class="order_main">
				<view v-for="item in audits" :key="item.id" class="order_item">
					<view v-if="item.isAudit==0">你的商品（ {{item.productName}} ）审核未通过！</view>
					<view v-else>你的商品（ {{item.productName}} ）审核通过！</view>
					<view v-if="item.isAudit==0">审核意见：{{item.auditMsg}}</view>
				</view>
			</view>
	</view>
</template>

<script>
	import {
		requestUtil
	} from '../../utils/request.js';
	import {
		apiBaseUrl
	} from "../../utils/config.js";
	export default {
		data() {
			return {
				audits:[]
			};
		},
		methods: {

			/**
			 * 获取订单
			 */
			async getAudits() {
				
				const res = await requestUtil({
					url: '/my/order/auditList',
					data: {userId:uni.getStorageSync('userId')}
				});
				this.audits = [...res.orderList];
			}
		},
		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow: function() {
			this.getAudits();
		},

		/**
		 * 生命周期函数--监听页面隐藏
		 */
		onHide: function() {
		},
		/**
		 * 生命周期函数--监听页面卸载
		 */
		onUnload: function() {
		},
		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {
			// 手动关闭等待效果
			wx.stopPullDownRefresh({

			})
		},
		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {
		},
		
		/**
		 * 用户点击右上角分享
		 */
		onShareAppMessage: function() {
		
		}

	}
</script>

<style lang="less">
	.head-nav{
		  display: flex;
		.title_item{
			display: flex;
			justify-content: center;
			align-items: center;
			flex:1;
			padding: 15rpx 0;
		}
		.activite{
			color: #FF740B;
			border-bottom: 5rpx solid currentColor;
		
		}
	}
	.order_main {
		.order_item {
			padding: 20rpx;
			border-bottom: 1rpx solid #ccc;
			color: #666;
			margin-top: 10rpx;
			.order_no_row {
				display: flex;
				justify-content: space-between;
				padding: 10rpx 0;

				.order_no_text {}

				.order_no_value {}
			}

			.order_price_row {
				display: flex;
				justify-content: space-between;
				padding: 10rpx 0;

				.order_price_text {}

				.order_price_value {
					color: var(--themeColor);
					font-size: 32rpx;
				}
			}

			.order_time_row {
				display: flex;
				justify-content: space-between;
				padding: 10rpx 0;

				.order_time_text {}

				.order_time_value {}
			}
		}
	}
</style>
