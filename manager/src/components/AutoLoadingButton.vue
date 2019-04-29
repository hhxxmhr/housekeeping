<template>
    <button
            class="el-button"
            @click="handleClick"
            :disabled="buttonDisabled || buttonLoading"
            :autofocus="autofocus"
            :type="nativeType"
            :class="[
      type ? 'el-button--' + type : '',
      buttonSize ? 'el-button--' + buttonSize : '',
      {
        'is-disabled': buttonDisabled,
        'is-loading': buttonLoading,
        'is-plain': plain,
        'is-round': round,
        'is-circle': circle
      }
    ]"
    >
        <i class="el-icon-loading" v-if="buttonLoading"></i>
        <i :class="icon" v-if="icon && !buttonLoading"></i>
        <span v-if="$slots.default"><slot></slot></span>
    </button>
</template>
<script>
    export default {
        name: 'at-button',

        inject: {
            elForm: {
                default: ''
            },
            elFormItem: {
                default: ''
            }
        },
        data(){
          return{
              autoLoading:false,
              autoDisable:false,
          }
        },

        props: {
            type: {
                type: String,
                default: 'default'
            },
            size: String,
            icon: {
                type: String,
                default: ''
            },
            nativeType: {
                type: String,
                default: 'button'
            },
            disabled: Boolean,
            loading: Boolean,
            plain: Boolean,
            autofocus: Boolean,
            round: Boolean,
            circle: Boolean,
            autoDisableAsLoading:Boolean,
            confirmTitle:{type:String,default:'提示'},
            confirmText:String,
            confirmType:{type:String,default:'warning'},
            confirmBtnOk:{
                type:String,
                default:'确定'
            },confirmBtnCancel:{
                type:String,
                default:'取消'
            }
        },

        computed: {
            _elFormItemSize() {
                return (this.elFormItem || {}).elFormItemSize;
            },
            buttonSize() {
                return this.size || this._elFormItemSize || (this.$ELEMENT || {}).size;
            },
            buttonLoading(){
                return this.autoLoading||this.loading;
            },
            buttonDisabled() {
                return this.autoDisable|| this.disabled || (this.elForm || {}).disabled;
            }
        },

        methods: {
            handleClick() {
                if(this.confirmText){
                    this.$confirm(this.confirmText, this.confirmTitle, {
                        confirmButtonText: this.confirmBtnOk,
                        cancelButtonText: this.confirmBtnCancel,
                        type:this.confirmType
                    }).then(() => {
                        this.$emit('click', this);
                    }).catch(() => {
                    });
                }else{
                    this.$emit('click', this);
                }
            }
        }
    };
</script>
