<template>
  <a-modal
    ref="modal"
    :class="getClass(modalClass)"
    :style="getStyle(modalStyle)"
    :visible="visible"
    v-bind="_attrs"
    v-on="$listeners"
    :confirm-loading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :destroyOnClose="destroyOnClose"
  >
    <slot></slot>
        <!--有设置标题-->
        <template v-if="!isNoTitle" slot="title">
          <a-row class="j-modal-title-row" type="flex">
            <a-col class="left">
              <slot name="title">{{ title }}</slot>
            </a-col>
            <a-col v-if="switchFullscreen" class="right" @click="toggleFullscreen">
              <a-button class="ant-modal-close ant-modal-close-x" ghost type="link" :icon="fullscreenButtonIcon"/>
            </a-col>
          </a-row>
        </template>
        <!--没有设置标题-->
        <template v-else slot="title">
          <a-row class="j-modal-title-row" type="flex">
            <a-col v-if="switchFullscreen" class="right" @click="toggleFullscreen">
              <a-button class="ant-modal-close ant-modal-close-x" ghost type="link" :icon="fullscreenButtonIcon"/>
            </a-col>
          </a-row>
        </template>

        <!-- 处理 scopedSlots -->
        <template v-for="slotName of scopedSlotsKeys" :slot="slotName">
          <a-spin :spinning="confirmLoadingTwo">
            <slot :name="slotName"></slot>
          </a-spin>
        </template>

        <!-- 处理 slots -->
        <template v-for="slotName of slotsKeys" v-slot:[slotName]>
          <a-spin :spinning="confirmLoadingTwo">
            <slot :name="slotName"></slot>
          </a-spin>
        </template>
  </a-modal>
</template>

<script>

import { getClass, getStyle } from '@/utils/props-util'
import { triggerWindowResizeEvent } from '@/utils/util'
import ModalDragMixins from './ModalDragMixins'

export default {
    name: 'JModal',
    mixins: [ModalDragMixins],
    props: {
      title: String,
      // 可使用 .sync 修饰符
      visible: Boolean,
      confirmLoadingTwo: Boolean,
      // 是否开启拖拽
      draggable: Boolean,
      // 是否全屏弹窗，当全屏时无论如何都会禁止 body 滚动。可使用 .sync 修饰符
      fullscreen: {
        type: Boolean,
        default: false
      },
      // 是否允许切换全屏（允许后右上角会出现一个按钮）
      switchFullscreen: {
        type: Boolean,
        default: false
      },
      // 点击确定按钮的时候是否关闭弹窗
      okClose: {
        type: Boolean,
        default: true
      },
      // 关闭时销毁弹窗内容
      destroyOnClose: {
        type: Boolean,
        default: true
      },
    },
    data() {
      return {
        // 内部使用的 slots ，不再处理
        confirmLoading: false,
        usedSlots: ['title'],
        // 实际控制是否全屏的参数
        innerFullscreen: this.fullscreen,
      }
    },
    computed: {
      // 一些未处理的参数或特殊处理的参数绑定到 a-modal 上
      _attrs() {
        let attrs = { ...this.$attrs }
        // 如果全屏就将宽度设为 100%
        if (this.innerFullscreen) {
          attrs['width'] = '100%'
        }
        return attrs
      },
      modalClass() {
        return {
          'j-modal-box': true,
          'fullscreen': this.innerFullscreen,
          'no-title': this.isNoTitle,
          'no-footer': this.isNoFooter,
        }
      },
      modalStyle() {
        let style = {}
        // 如果全屏就将top设为 0
        if (this.innerFullscreen) {
          style['top'] = '0'
        }
        return style
      },
      isNoTitle() {
        return !this.title && !this.allSlotsKeys.includes('title')
      },
      isNoFooter() {
        return this._attrs['footer'] === null
      },
      slotsKeys() {
        return Object.keys(this.$slots).filter(key => !this.usedSlots.includes(key))
      },
      scopedSlotsKeys() {
        return Object.keys(this.$scopedSlots).filter(key => !this.usedSlots.includes(key))
      },
      allSlotsKeys() {
        return Object.keys(this.$slots).concat(Object.keys(this.$scopedSlots))
      },
      // 切换全屏的按钮图标
      fullscreenButtonIcon() {
        return this.innerFullscreen ? 'fullscreen-exit' : 'fullscreen'
      },
    },
    watch: {
      visible() {
        if (this.visible) {
          this.innerFullscreen = this.fullscreen
        }
      },
      innerFullscreen(val) {
        this.$emit('update:fullscreen', val)
      },
    },
    methods: {

      getClass(clazz) {
        return { ...getClass(this), ...clazz }
      },
      getStyle(style) {
        return { ...getStyle(this), ...style }
      },

      close() {
        this.$emit('update:visible', false)
      },

      handleOk() {
        this.confirmLoading = true
        setTimeout(() => {
          this.confirmLoading = false
        }, 1500)
        if (this.okClose) {
          this.close()
        }
      },
      handleCancel() {
        this.close()
      },

      /** 切换全屏 */
      toggleFullscreen() {
        this.innerFullscreen = !this.innerFullscreen
        triggerWindowResizeEvent()
        // 开启拖拽后的特殊处理
        if (this.draggable) {
          // 全屏的时候禁止拖动
          if (this.innerFullscreen) {
            // 还原弹窗的位置为0,0
            this.setModalPosition(0, 0, false)
            this.dragSettings.headerEl.style.cursor = null
          } else {
            // 取消全屏的时候，将弹窗移动到上次记录的位置
            this.resetModalPosition()
            this.dragSettings.headerEl.style.cursor = 'move'
          }
        }
      },

    }
  }
</script>

<style lang="less">

  .j-modal-box {
    &.fullscreen {
      top: 0;
      left: 0;
      padding: 0;

      // 兼容1.6.2版本的antdv
      & .ant-modal {
        top: 0;
        padding: 0;
        height: 100vh;
      }

      & .ant-modal-content {
        height: 100vh;
        border-radius: 0;

        & .ant-modal-body {
          /* title 和 footer 各占 55px */
          height: calc(100% - 55px - 55px);
          overflow: auto;
        }
      }

      &.no-title, &.no-footer {
        .ant-modal-body {
          height: calc(100% - 55px);
        }
      }
      &.no-title.no-footer {
        .ant-modal-body {
          height: 100%;
        }
      }
    }

    .j-modal-title-row {
      .left {
        width: calc(100% - 56px - 56px);
      }

      .right {
        width: 56px;
        position: inherit;

        .ant-modal-close {
          right: 56px;
          color: rgba(0, 0, 0, 0.45);

          &:hover {
            color: rgba(0, 0, 0, 0.75);
          }
        }
      }
    }
    &.no-title{
      .ant-modal-header {
        padding: 0px 24px;
        border-bottom: 0px !important;
      }
    }
  }

  @media (max-width: 767px) {
    .j-modal-box.fullscreen {
      margin: 0;
      max-width: 100vw;
    }
  }
</style>
