<template>
	<view class="addProduct">
		<!-- 		<uni-forms :modelValue="formData">
			<uni-forms-item label="姓名" name="name">
				<uni-easyinput type="text" v-model="formData.name" placeholder="请输入姓名" />
			</uni-forms-item>
			<uni-forms-item label="年龄" name="name">
				<input type="text" v-model="formData.age" placeholder="请输入年龄" />
			</uni-forms-item>
			<uni-forms-item required name="hobby" label="兴趣爱好">
				<uni-data-checkbox multiple v-model="formData.hobby" :localdata="hobby" />
			</uni-forms-item>
		</uni-forms>
		<button @click="submitForm">Submit</button> -->
		<uni-forms ref="form" :model="form" :rules="rules" label-width="100px">
			<uni-forms-item label="商品名称" prop="name">
				<uni-easyinput v-model="form.name" style="width: 400px" />
			</uni-forms-item>

			<uni-forms-item label="商品价格" prop="price">
				<uni-easyinput v-model="form.price" style="width: 100px" placeholder="单位:元"/>
			</uni-forms-item>

			<uni-forms-item label="商品几成新" prop="stock">
				<uni-easyinput v-model="form.stock" style="width: 100px" type="number" maxlength="1" placeholder="0~9"/>
			</uni-forms-item>

			<uni-forms-item label="商品类别">
				<uni-data-select v-model="bigTypeId" :localdata="bigTypeList" :clear="false" class="m-2"
					placeholder="请选择商品大类..." @change="handleBigTypeChange">
					<!--          <el-option
		              v-for="item in bigTypeSelectOptions"
		              :key="item.id"
		              :label="item.name"
		              :value="item.id"></el-option> -->
				</uni-data-select>
				&nbsp;&nbsp;
				<uni-data-select v-model="form.type.id" :localdata="smallTypeList" class="m-2" placeholder="请选择商品小类...">
					<!--          <el-option
		              v-for="item in smallTypeSelectOptions"
		              :key="item.id"
		              :label="item.name"
		              :value="item.id"></el-option> -->
				</uni-data-select>
			</uni-forms-item>
			<uni-forms-item label="商品描述" prop="description">
				<uni-easyinput v-model="form.description" type="textarea" :rows="4" />
			</uni-forms-item>
			<uni-forms-item label="商品介绍">
				<uni-easyinput v-model="form.productIntroImgs" type="textarea" :rows="4" />
			</uni-forms-item>
			<!-- 		      <QuillEditor
		          v-model:content="form.productIntroImgs"
		          contentType="html"
		          toolbar="full"
		          theme="snow"
		          style="height:200px"
		      /> -->
			<uni-forms-item label="商品参数">
				<uni-easyinput v-model="form.productParaImgs" type="textarea" :rows="4" />
			</uni-forms-item>
			<!-- 		      <QuillEditor
		          v-model:content="form.productParaImgs"
		          contentType="html"
		          toolbar="full"
		          theme="snow"
		          style="height:200px"
		      /> -->
			<uni-forms-item label="商品图片">
				<uni-file-picker v-model="imageValue" fileMediatype="image" mode="grid" :limit="5" @select="select"
					@progress="progress" @success="success" @fail="fail" />
			</uni-forms-item>
		</uni-forms>

		<!-- <template #footer>
		      <span class="dialog-footer">
		
		        <button  @click="handleClose">取消</button>
		     <button type="primary" @click="handleConfirm">确认</button>
		      </span>
		    </template> -->
		<button @click="handleConfirm"  class="order_pay_wrap" >上传商品</button>
	</view>
</template>

<script>
	import {
		requestUtil,
		getSmallType
	} from "../../utils/request.js";
	import {
		apiBaseUrl
	} from "../../utils/config.js";
	export default {
		data() {
			return {
				// 表单数据
				form: {
					id: -1,
					name: '',
					price: null,
					stock: null,
					type: {
						id: 0
					},
					proPic: '',
					productIntroImgs: '',
					productParaImgs: '',
					description: '',
					isAudit:0,
					fromUser:1
				},
				imgform: [],
				rules: {
					name: [{
						required: true,
						message: '请输入商品小类名称！',
					}],
					price: [{
							required: true,
							message: '请输入商品价格!'
						},
						{
							type: 'number',
							message: '商品价格是数值类型！',
							transform: (value) => Number(value)
						},
					],
					stock: [{
							required: true,
							message: '请输入商品库存!'
						},
						{
							type: 'number',
							message: '商品库存是数值类型！',
							transform: (value) => Number(value)
						},
					],
					description: [{
						required: true,
						message: '请输入商品小类描述！',
					}, ]
				},
				bigTypeId: 0,
				smallTypeId: 0,
				bigTypeList: [],
				bigTypeSelectOptions: [],
				smallTypeList: [],
				smallTypeSelectOptions: [],
				typeList: []
			}
		},
		onLoad() {
			getSmallType((res) => {
				this.typeList = res.message
				res.message.map(v => {
					this.bigTypeList.push({
						"value": v.id,
						"text": v.name
					})
				})
			})
		},
		methods: {
			async initBigTypeSelectList() {
				// const res=await axios.get("admin/bigType/listAll");
				// bigTypeSelectOptions.value=res.data.bigTypeList;
				const res = await requestUtil({
					url: '/my/order/list',
					data: this.QueryParams
				});
			},

			async initSmallTypeSelectList(bigTypeId) {
				const res = await axios.get("admin/smallType/listAll/" + bigTypeId);
				smallTypeSelectOptions.value = res.data.smallTypeList;
			},

			handleBigTypeChange(e) {
				this.smallTypeList = []
				let li = []
				this.typeList.map(v => {
					if (v.id === e) {
						li = v.smallTypeList
					}
				})
				li.map(v => {
					this.smallTypeList.push({
						"value": v.id,
						"text": v.name
					})
				})
			},
			async handleConfirm() {
				if (this.form.type.id == 0) {
					uni.showToast({
						title: '请选择商品类别',
						icon: 'none',
						duration: 2000
					});
					return;
				} else {
					console.log(this.form)
					// this.form.proPic = this.imgform[0]?this.imgform[0].image:'';
					const result = await requestUtil({
						url: "/product/save",
						data: this.form,
						method: "post"
					}).then((res) => {
						this.imgform.map(v => {
							v.productId = res['id']
						})
						this.upload()
						console.log("res:", res)
					})
				}

				// let result = await axios.post("admin/product/save", form.value);
				// let data = result.data;
				// if (data.code == 200) {
				// 	ElMessage.success("执行成功！")
				// 	formRef.value.resetFields();
				// 	emits("initProductList")
				// 	handleClose();
				// } else {
				// 	ElMessage.error(data.msg);
				// }
			},
			handleClose: () => {

			},
			//图片上传
			//图片列表上传
			async upload() {
				if (this.imgform != []) {
					const result = await requestUtil({
						url: "/product/productSwiperImage/add",
						data: this.imgform,
						method: "post"
					});
					let data = result.data;
					// if (data.code == 200) {

					// } else {

					// }
				}
			},
			// 获取上传状态
			select(e) {
				console.log('选择文件：', e)
				const tempFilePaths = e.tempFilePaths; //e是获取的图片源
				if (this.imgform.length === 0) {
					uni.uploadFile({
						header: {
							"token": uni.getStorageSync('token')
						},
						url: apiBaseUrl + '/product/uploadImage', //上传图片的后端接口
						filePath: tempFilePaths[0],
						name: 'file',
						success: res => {
							this.form.proPic = res.data ? res.data : '';
							console.log(res, "头像1")
						}
					})
				}
				uni.uploadFile({
					header: {
						"token": uni.getStorageSync('token')
					},
					url: apiBaseUrl + '/product/productSwiperImage/uploadImage', //上传图片的后端接口
					filePath: tempFilePaths[0],
					name: 'file',
					success: res => {
						let avatar = res.data
						this.imgform.push({
							productId: -1,
							sort: 0,
							image: avatar,
						})
						console.log(this.imgform, "头像")
					}
				})
			},
			// 获取上传进度
			progress(e) {
				console.log('上传进度：', e)
			},

			// 上传成功
			success(e) {
				console.log('上传成功')
			},

			// 上传失败
			fail(e) {
				console.log('上传失败：', e)
			}
		}
	}
</script>

<style lang="less">
	.addProduct {

		.example {
			padding: 15px;
			height: 100vh;
			background-color: #fff;

			.vue-ref {
				width: 100rpx;
				height: 100rpx;
			}
		}

		.segmented-control {
			margin-bottom: 15px;
		}

		.button-group {
			margin-top: 15px;
			display: flex;
			justify-content: space-around;
		}

		.form-item {
			display: flex;
			align-items: center;
		}

		.button {
			display: flex;
			align-items: center;
			height: 35px;
			margin-left: 10px;
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
</style>
