<template>
	<view class="tabs">
		<view class="tabs_title">
			<view @tap="handleItemTap" :data-index="index" v-for="item,index in properties.tabs" :key="item.id"
				:class="['title_item', item.isActive?'active':'']">
				{{item.value}}
			</view>
		</view>
		<view class="tabs_content">
			<slot></slot>
		</view>
	</view>
</template>

<script>
	export default {
		name: "Tabs",
		data() {
			return {
				properties: {
					tabs: Array,
					value: []
				}
			};
		},

		/**
		 * 组件的方法列表
		 */
		methods: {
			handleItemTap(e) {
				const {
					index
				} = e.currentTarget.dataset;
				console.log("index=" + index)
				// 触发 父组件中的自定义事件
				this.triggerEvent("tabsItemChange", {
					index
				});
			}
		}
	}
</script>

<style scoped lang="less">
	.tabs .tabs_title {
		display: flex;
	}

	.tabs .tabs_title .title_item {
		display: flex;
		justify-content: center;
		align-items: center;
		flex: 1;
		padding: 15rpx 0;
	}

	.tabs .tabs_title .active {
		color: var(--themeColor);
		border-bottom: 5rpx solid currentColor;
	}
</style>
