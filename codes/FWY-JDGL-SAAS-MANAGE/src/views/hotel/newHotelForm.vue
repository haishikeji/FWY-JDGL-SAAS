<template>
    <a-form :form="form" :label-col="{ span: 5 }" :wrapper-col="{ span: 17 }" @submit="handleSubmit">
        <a-form-item label="酒店名称">
            <a-input class="short-anl" placeholder="请填写酒店名称"
                v-decorator="['name', { rules: [{ required: true, message: '请填写酒店名称！' }] }]" />
        </a-form-item>

        <a-form-item label="酒店联系人">
            <a-input class="short-anl" placeholder="请填写酒店联系人"
                v-decorator="['linkName', { rules: [{ required: true, message: '请填写酒店联系人！' }] }]" />
        </a-form-item>

        <a-form-item label="酒店联系人电话">
            <a-input class="short-anl" placeholder="请填写酒店联系人电话"
                v-decorator="['linkTel', { rules: [{ required: true, message: '请填写酒店联系人电话！' }] }]" />
        </a-form-item>

        <a-form-item label="酒店电话">
            <a-input class="short-anl" placeholder="请填写酒店电话"
                v-decorator="['tel', { rules: [{ required: true, message: '请填写酒店电话！' }] }]" />
        </a-form-item>

        <a-form-item label="酒店地址">
            <a-input class="short-anl" placeholder="请填写酒店地址"
                v-decorator="['address', { rules: [{ required: true, message: '请填写酒店地址！' }] }]" />
        </a-form-item>


        <a-form-item label="经纬度">
            <a-input style="width: 50%;" placeholder="经纬度"
                v-decorator="['latlng', { rules: [{ required: true, message: '请填写经纬度！' }] }]" />
            <a-button-group>
                <a-button type="primary"> <a-icon type="thunderbolt" />快速获取 </a-button>
                <a-button type="primary"><a-icon type="environment" />地图选点</a-button>
            </a-button-group>
        </a-form-item>


        <a-form-item label="酒店星级">
            <a-radio-group v-decorator="['star',{ rules: [{ required: true, message: '请选择星级！' }] }]">
                <a-radio-button value="1">
                    一星及以下/经济
                </a-radio-button>
                <a-radio-button value="2">
                    二星/实惠
                </a-radio-button>
                <a-radio-button value="3">
                    三星/舒适
                </a-radio-button>
                <a-radio-button value="4">
                    四星/高档
                </a-radio-button>
                <a-radio-button value="5">
                    五星/豪华
                </a-radio-button>
            </a-radio-group>
        </a-form-item>

        <a-form-item label="总房间数">
            <a-input-number placeholder="1~9999" style="width: 180px;" :min="1" :max="9999"  v-decorator="['roomCount', { rules: [{ required: true, message: '1~9999' }], }]"/>
        </a-form-item>




        <a-form-item label="集团品牌">
            <a-select class="short-anl" v-decorator="[
                'companyBrand', ]" placeholder="请选择" @change="handleSelectChange">
                <a-select-option value="2">
                    七天连锁酒店
                </a-select-option>
                <a-select-option value="3">
                    一曼酒店
                </a-select-option>
                <a-select-option value="4">
                    长房地产
                </a-select-option>
            </a-select>
        </a-form-item>

        <a-form-model-item  label="酒店介绍" class="j-field-content">
            <j-editor v-model="form_editor_content" />
          </a-form-model-item>

        <!-- <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
            <a-button type="primary" html-type="submit">
                Submit
            </a-button>
        </a-form-item> -->
    </a-form>
</template>

<script>
import { newHotel } from '../../api/hotel';

export default {
    data() {
        return {
            formLayout: 'horizontal',
            form: this.$form.createForm(this, { name: 'coordinated' }),
            form_editor_content:""
        };
    },
    methods: {
        handleSubmit(e) {
            this.$router.go(0);
            // e.preventDefault();
            this.form.validateFields((err, values) => {
                if (!err) {
                    values['lat'] = values.latlng.split(',')[0]
                    values['lng'] = values.latlng.split(',')[1]
                    if(this.form_editor_content) values['introduction'] = this.form_editor_content
                    newHotel(values).then(res=>{
                        this.$emit('addSuccess');
                    })
                    console.log('Received values of form: ', values);
                }
            });
        },
        handleSelectChange(value) {
            console.log(value);
            this.form.setFieldsValue({
                note: `Hi, ${value === 'male' ? 'man' : 'lady'}!`,
            });
        },
    },
}
</script>

<style>
.short-anl{
    width: 70%;
}
</style>
