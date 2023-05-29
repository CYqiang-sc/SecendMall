<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		globalData:{
			index:-1,
			aaaaa:21
		}
	}
</script>

<style>
	/*每个页面公共css */
	@import "./styles/iconfont.css";
	
	/* 主题颜色，字体大小 */
	*{
		padding: 0;
		margin: 0;
		box-sizing: border-box;
		--themeColor:#ff5700;
		font-size: 28rpx;
	}
	uni-page-body,
	html,
	body {
		height: 100%;
	}
</style>
