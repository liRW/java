#1.鉴定包冲突
参考：http://stamen.iteye.com/blog/1554987

#Word2007Utils 第一种使用方式详解
参照：https://blog.csdn.net/adalf90/article/details/78813652
##语法
所有的语法结构都是以 {{ 开始，以 }} 结束(在下一版本中，语法将支持自定义)，文档的样式继承模板标签的样式，也可以在渲染数据中指定,实现了样式的最大自由化。
* {{template}}
普通文本，渲染数据为：String或者TextRenderData
* {{@template}}
图片,渲染数据为：PictureRenderData
* {{#template}}
表格，渲染数据为：TableRenderData
* {{*template}}
列表，渲染数据为：NumbericRenderData


#提交代码
echo "# 工具类" >> README.md
git init
git add README.md
git commit -m "工具类"
git remote add origin https://github.com/liRW/java.git
git push -u origin master