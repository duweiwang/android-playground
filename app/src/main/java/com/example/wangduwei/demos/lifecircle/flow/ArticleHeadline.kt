package com.example.wangduwei.demos.lifecircle.flow

/**
 * @author 杜伟
 * @date 2021/1/4 下午9:14
 *
 */
data class ArticleHeadline(val id:Int)
data class UserData(val id:Int){
    fun isFavoriteTopic(articleHeadline: ArticleHeadline):Boolean{
        return false
    }
}