package com.example.wangduwei.demos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment

/**
 * @author 杜伟
 * @date 2022/4/19 5:00 PM
 *
 */
@PageInfo(description = "Compose", navigationId = R.id.fragment_compose)
class ComposeFragment : BaseSupportFragment() {
    val colors = listOf(
        Color(0xFFffd7d7.toInt()),
        Color(0xFFffe9d6.toInt()),
        Color(0xFFfffbd0.toInt()),
        Color(0xFFe3ffd9.toInt()),
        Color(0xFFd0fff8.toInt())
    )

    val demoList = listOf(
        "text",
        "image"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent(content = {
                LazyColumnItemsScrollableComponent(demoList)
            })
        }
    }

    @Composable
    fun LazyColumnItemsScrollableComponent(demoList: List<String>) {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(items = demoList, itemContent = { demoName ->
                val index = demoList.indexOf(demoName)


                Row(modifier = Modifier.fillParentMaxWidth()) {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        backgroundColor = colors[index % colors.size],
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(6.dp)
                    ) {
                        Text(
                            text = demoName,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                }
            })
        }
    }


}