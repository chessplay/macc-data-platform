<template>
<div>
    <span class='header'></span>
    <slot>
        <span></span>
    </slot>
</div>
</template>
<script>
export default {

}
</script>
<style lang="less" scoped>
.header {
    float: left;
    display: block;
    height: 22px;
    width: 3px;
    background-color: #2b6afd;
}
.title span{
    margin-right: 6px;
}
</style>