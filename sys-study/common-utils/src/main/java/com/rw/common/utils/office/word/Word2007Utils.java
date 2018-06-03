package com.rw.common.utils.office.word;

import com.deepoove.poi.XWPFTemplate;
import com.google.common.collect.Maps;
import com.rw.common.utils.often.ConfigUtil;
import com.rw.common.utils.often.UUIDUtils;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author MR. Li
 * @Description:
 * @Date: Created in 12:52 2018/5/14
 * @Modified By:
 * 参考：https://blog.csdn.net/chen497147884/article/details/79678513
 *       https://www.jianshu.com/p/7af902234eb9
 *       https://www.cnblogs.com/tianyublog/p/6957953.html
 */
public class Word2007Utils {

    private static final Logger logger = LoggerFactory.getLogger(Word2007Utils.class);
    /*************************************第一种方式 语法请查看本工程的readme.md 文件**************************************/

    /**
     * 根据模板解析word
     *
     * @param templatePath
     * @param destPath 注意目标文件夹不存在的时候不会自动创建文件夹 需要手动穿件
     * @param datas
     */
    public static void parseTemplate(String templatePath, String destPath, Map<String, Object> datas) {
        try {  
            XWPFTemplate template = XWPFTemplate.compile(templatePath).render(datas);
            FileOutputStream out = new FileOutputStream(destPath);
            template.write(out);
            template.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
           logger.info("解析模板出错", e);
        }
    }

    
    /*
    public static boolean wordToHtml(String templatePath, String destPath) {
        if (templatePath == null) {// word文档路径为空
            System.err.println("error:path of word document is null");
            return false;
        }
        final File wordFile = new File(templatePath).getAbsoluteFile();
        final File htmlFile = new File(destPath);
        try {
            final InputStream inputStream = new FileInputStream(wordFile);// 输入流
            final XWPFDocument document = new XWPFDocument(inputStream);// 读取word文档
            inputStream.close();// 关闭输入流
            final XHTMLOptions options = XHTMLOptions.create();// 创建选项
            options.setImageManager(new ImageManager(wordFile.getParentFile(), "PoiImages"));// 设置图片文件夹保存的路径以及文件夹名称
            final OutputStream outputStream = new FileOutputStream(htmlFile);// 输出流
            XHTMLConverter.getInstance().convert(document, outputStream, options);// word文档转html
            System.out.println("html:" + htmlFile.getAbsolutePath());
            outputStream.close();// 关闭输出流
            document.close();// 关闭文档
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
*/

    //********************************第二种方式  暂时不可以用******************************
    /**
     * 根据模板生成新word文档
     * 判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
     *
     * @param inputUrl  模板存放地址
     * @param outputUrl 新文档存放地址
     * @param textMap   需要替换的信息集合
     * @param tableList 需要插入的表格信息集合
     * @return 成功返回true, 失败返回false
     */
    @Deprecated
    public static boolean changWord(String inputUrl, String outputUrl, Map<String, String> textMap, List<String[]> tableList) {
        //模板转换默认成功
        boolean changeFlag = true;
        try {
            //获取docx解析对象
            XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
            //解析替换文本段落对象
            if(null!=textMap) {
                Word2007Utils.changeText(document, textMap);
            }
            //解析替换表格对象  暂时不需要插入表格数据
            if(null!=tableList) {
                Word2007Utils.changeTable(document, textMap, tableList);
            }
            //生成新的word
            File file = new File(outputUrl);
            FileOutputStream stream = new FileOutputStream(file);
            document.write(stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
            changeFlag = false;
        }

        return changeFlag;

    }

    /**
     * 替换段落文本
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void changeText(XWPFDocument document, Map<String, String> textMap) {
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        System.out.println("==========55555====paragraphs:"+paragraphs.size());
        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();

            if (checkText(text)) {
                List<XWPFRun> runs = paragraph.getRuns();//这里的分词有问题 有缺陷
                for (XWPFRun run : runs) {
                    //替换模板原来位置
                    run.setText(changeValue(run.toString(), textMap), 0);
                }
            }
        }

    }

    /**
     * 替换表格对象方法
     *
     * @param document  docx解析对象
     * @param textMap   需要替换的信息集合
     * @param tableList 需要插入的表格信息集合
     */
    public static void changeTable(XWPFDocument document, Map<String, String> textMap,
                                   List<String[]> tableList) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            //只处理行数大于等于2的表格，且不循环表头
            XWPFTable table = tables.get(i);
            if (table.getRows().size() > 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (checkText(table.getText())) {
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(rows, textMap);
                } else {
//                  System.out.println("插入"+table.getText());  暂时不需要插入
                    //   insertTable(table, tableList);
                }
            }
        }
    }


    /**
     * 遍历表格
     *
     * @param rows    表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows, Map<String, String> textMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if (checkText(cell.getText())) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            run.setText(changeValue(run.toString(), textMap), 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 为表格插入数据，行数不够添加新行
     *
     * @param table     需要插入数据的表格
     * @param tableList 插入数据集合
     */
    public static void insertTable(XWPFTable table, List<String[]> tableList) {
        //创建行,根据需要插入的数据添加新行，不处理表头
        for (int i = 1; i < tableList.size(); i++) {
            XWPFTableRow row = table.createRow();
        }
        //遍历表格插入数据
        List<XWPFTableRow> rows = table.getRows();
        for (int i = 1; i < rows.size(); i++) {
            XWPFTableRow newRow = table.getRow(i);
            List<XWPFTableCell> cells = newRow.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                cell.setText(tableList.get(i - 1)[j]);
            }
        }

    }


    /**
     * 判断文本中时候包含$
     *
     * @param text 文本
     * @return 包含返回true, 不包含返回false
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.indexOf("$") != -1) {
            check = true;
        }
        return check;

    }

    /**
     * 匹配传入信息集合与模板
     *
     * @param value   模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String value, Map<String, String> textMap) {
        Set<Map.Entry<String, String>> textSets = textMap.entrySet();
        for (Map.Entry<String, String> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = "${" + textSet.getKey() + "}";
            if (value.indexOf(key) != -1) {
                value = textSet.getValue();
            }
        }
        //模板未匹配到区域替换为空
        if (checkText(value)) {
            value = "";
        }
        return value;
    }

    /**
     * 仅支持word 2007以上的版本
     * 合并多个word 生成一个文档
     *
     * @param files 文件路径
     */
    public static void mergeWord2007(Map<String,Object>  files) {

        InputStream in1 = null;
        InputStream in2 = null;
        OPCPackage src1Package = null;
        OPCPackage src2Package = null;

        OutputStream dest = null;
        try {
            dest = new FileOutputStream(files.get("destFile").toString());
            in1 = new FileInputStream(files.get("file1").toString());
            in2 = new FileInputStream(files.get("file2").toString());
            src1Package = OPCPackage.open(in1);
            src2Package = OPCPackage.open(in2);


            XWPFDocument src1Document = new XWPFDocument(src1Package);
            CTBody src1Body = src1Document.getDocument().getBody();
            XWPFDocument src2Document = new XWPFDocument(src2Package);
            CTBody src2Body = src2Document.getDocument().getBody();
            appendBody(src1Body, src2Body);
            src1Document.write(dest);
            logger.info("......模板合并成功......");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("......模板合并失败......");
        }
    }


    private static void appendBody(CTBody src, CTBody append) throws Exception {
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = append.xmlText(optionsOuter);
        String srcString = src.xmlText();
        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String sufix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        CTBody makeBody = CTBody.Factory.parse(prefix + mainPart + addPart + sufix);
        src.set(makeBody);
    }




    public static void main(String[] a) {  
    	//原始路径
    	String original_templatePath = Word2007Utils.class.getClassLoader().getResource("").getPath()
				.concat(ConfigUtil.getConfigValue("TEMPLET_PATCH")).concat("/");
    	String original_destPath = original_templatePath+ConfigUtil.getConfigValue("TEMP_PATCH").concat("/");
    	Map<String,Object>  datas = Maps.<String,Object>newHashMap();
    	String template = null;
    	String templatePath =null;
    	String destPath = null;
    	/**
    	template = ConfigUtil.getConfigValue("SYNTHETIC_WORD");
    	templatePath = original_templatePath.concat(template);
    	destPath = original_destPath.concat(UUIDUtils.getUUID().concat("_").concat(template));
    	logger.info("---templatePath->{}",templatePath);
    	logger.info("---destPath->{}",destPath);
      //模板+数据   	
    	datas.put("N100", "老李");
    	datas.put("N101", 100);
    	datas.put("N102", 200);
    	datas.put("N103", "哈哈哈");
    	datas.put("N104", new Date());
    	parseTemplate(templatePath, destPath, datas);
    	**/
    	
    	
    	//合并两个文件
    	
    	datas.clear();
    	destPath  = original_destPath.concat(UUIDUtils.getUUID()).concat("_").concat("merge.docx");
    	logger.info("destPath----->{}",destPath);
    	datas.put("destFile",destPath);
    	datas.put("file1", original_templatePath+"merge_1.docx");
    	datas.put("file2", original_templatePath+"merge_2.docx");
    	mergeWord2007(datas);
    	

    }

}
