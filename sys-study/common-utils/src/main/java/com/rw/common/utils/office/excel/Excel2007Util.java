package com.rw.common.utils.office.excel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.rw.common.utils.annotation.ExcelAttribute;
import com.rw.common.utils.exception.ExcelException;
import com.rw.common.utils.model.BatchExport;
import com.rw.common.utils.often.UUIDUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Mr. Li
 * @Description: 解析excel2007
 * @Date: Created in 16:58 2018/5/6
 * @Modified By:
 */
public class Excel2007Util<T> implements Serializable {
	private static final long serialVersionUID = 551970754610248636L;
	private static Logger logger = LoggerFactory.getLogger(Excel2007Util.class);

	private Class<T> clazz;

	public Excel2007Util(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 将excel表单数据源的数据导入到list
	 *
	 * @param sheetName
	 *            工作表的名称
	 * @param input
	 *            java输入流
	 */
	public List<T> getExcelToList(String sheetName, InputStream input) throws ExcelException {
		List<T> list = new ArrayList<T>();
		try {
			XSSFWorkbook book = new XSSFWorkbook(input);
			XSSFSheet sheet = null;
			// 如果指定sheet名,则取指定sheet中的内容.
			if (StringUtils.isNotBlank(sheetName)) {
				sheet = book.getSheet(sheetName);
			}
			// 如果传入的sheet名不存在则默认指向第1个sheet.
			if (sheet == null) {
				sheet = book.getSheetAt(0);
			}
			// 得到数据的行数
			int rows = sheet.getLastRowNum();
			// 有数据时才处理
			if (rows > 0) {
				// 得到类的所有field
				Field[] allFields = clazz.getDeclaredFields();
				// 定义一个map用于存放列的序号和field
				Map<String, Field> fieldsMap = new HashMap<String, Field>();
				for (int i = 0, index = 0; i < allFields.length; i++) {
					Field field = allFields[i];
					// 将有注解的field存放到map中
					if (field.isAnnotationPresent(ExcelAttribute.class)) {
						// 设置类的私有字段属性可访问
						field.setAccessible(true);
						// 获取排序
						ExcelAttribute excelAttribute = field.getAnnotation(ExcelAttribute.class);

						String column = excelAttribute.column();
						// 要么全部排序 要么全部不排序
						if (StringUtils.isNoneBlank(column)) {
							fieldsMap.put(column, field);

						} else {
							fieldsMap.put(index + "", field);
						}
						index++;
					}
				}

				// 获取总列数
				int colnum = sheet.getRow(0).getLastCellNum();// 总列数，根据第一行得来的;
				// 从第2行开始取数据,默认第一行是表头
				for (int i = 1; i <= rows; i++) {
					// 得到一行中的所有单元格对象.
					XSSFRow row = sheet.getRow(i);
					// Iterator<Cell> cells = row.cellIterator();//用迭代器 遇到空格将跳过
					// 去产生数据错位
					T entity = null;
					;
					// while (cells.hasNext()) {
					for (int index = 0; index < colnum; index++) {
						Cell cell = row.getCell(index);
						// 单元格中的内容.
						/* Cell cell = cells.next(); */
						String c = getCellValue(cell);
						// 如果不存在实例则新建
						entity = (entity == null ? clazz.newInstance() : entity);
						// 从map中得到对应列的field
						Field field = fieldsMap.get(index + "");
						if (field == null) {
							continue;
						}
						// 取得类型,并根据对象类型设置值.
						Class<?> fieldType = field.getType();
						if (fieldType == null) {
							continue;
						}
						if (String.class == fieldType) {
							field.set(entity, String.valueOf(c));
						} else if (BigDecimal.class == fieldType) {
							field.set(entity, BigDecimal.valueOf(Double.valueOf(c)));
						} else if (Date.class == fieldType) {
							// 日期最好格式化一下
							field.set(entity, c);
						} else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
							field.set(entity, Integer.parseInt(c));
						} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
							field.set(entity, Long.valueOf(c));
						} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
							field.set(entity, Float.valueOf(c));
						} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
							field.set(entity, Short.valueOf(c));
						} else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
							field.set(entity, Double.valueOf(c));
						} else if (Character.TYPE == fieldType) {
							if ((c != null) && (c.length() > 0)) {
								field.set(entity, Character.valueOf(c.charAt(0)));
							}
						}
						index++;

					}
					if (entity != null) {
						list.add(entity);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcelException("将excel表单数据源的数据导入到list异常!");

		}
		return list;
	}

	private String getCellValue(Cell cell) {
		String cellValue = "";
		if (null == cell) {
			return cellValue;
		}
		DecimalFormat df = new DecimalFormat("#");
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			cellValue = cell.getRichStringCellValue().getString().trim();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			cellValue = df.format(cell.getNumericCellValue()).toString();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			cellValue = cell.getCellFormula();
			break;
		default:
			cellValue = "";
		}
		return cellValue;
	}

	/**
	 * 将list数据源的数据导入到excel表单
	 *
	 * @param list
	 *            数据源
	 * @param sheetName
	 *            工作表的名称
	 * @return 文件路径
	 */
	public String getListToExcel(List<T> list, String sheetName, String fileName) throws ExcelException {
		try {
			// excel中每个sheet中最多有65536行
			int sheetSize = 65536;
			// 得到所有定义字段
			Field[] allFields = clazz.getDeclaredFields();
			List<Field> fields = new ArrayList<Field>();
			// 得到所有field并存放到一个list中
			for (Field field : allFields) {
				if (field.isAnnotationPresent(ExcelAttribute.class)) {
					fields.add(field);
				}
			}
			// 产生工作薄对象
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 取出一共有多少个sheet
			int listSize = 0;
			if (list != null && list.size() >= 0) {
				listSize = list.size();
			}
			double sheetNo = Math.ceil(listSize / sheetSize);
			for (int index = 0; index <= sheetNo; index++) {
				// 产生工作表对象
				XSSFSheet sheet = workbook.createSheet();
				// 设置工作表的名称.
				workbook.setSheetName(index, sheetName + index);
				XSSFRow row;
				XSSFCell cell;// 产生单元格
				row = sheet.createRow(0);// 产生一行
				/* *********普通列样式********* */
				XSSFFont font = workbook.createFont();
				XSSFCellStyle cellStyle = workbook.createCellStyle();
				font.setFontName("Arail narrow"); // 字体
				// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体宽度
				/* *********标红列样式********* */
				XSSFFont newFont = workbook.createFont();
				XSSFCellStyle newCellStyle = workbook.createCellStyle();
				newFont.setFontName("Arail narrow"); // 字体
				// newFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体宽度
				/* *************创建列头名称*************** */
				for (int i = 0; i < fields.size(); i++) {
					Field field = fields.get(i);
					ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
					int col = i;
					// 根据指定的顺序获得列号
					if (StringUtils.isNotBlank(attr.column())) {
						col = getExcelCol(attr.column());
					}
					// 创建列
					cell = row.createCell(col);
					if (attr.isMark()) {
						newFont.setColor(HSSFFont.COLOR_RED); // 字体颜色
						newCellStyle.setFont(newFont);
						cell.setCellStyle(newCellStyle);
					} else {
						font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
						cellStyle.setFont(font);
						cell.setCellStyle(cellStyle);
					}
					sheet.setColumnWidth(i,
							(int) ((attr.name().getBytes().length <= 4 ? 6 : attr.name().getBytes().length) * 1.5
									* 256));
					// 设置列中写入内容为String类型
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					// 写入列名
					cell.setCellValue(attr.name());
					// 如果设置了提示信息则鼠标放上去提示.
					if (StringUtils.isNotBlank(attr.prompt())) {
						setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);
					}
					// 如果设置了combo属性则本列只能选择不能输入
					if (attr.combo().length > 0) {
						setHSSFValidation(sheet, attr.combo(), 1, 100, col, col);
					}
				}
				/* *************创建内容列*************** */
				font = workbook.createFont();
				cellStyle = workbook.createCellStyle();
				int startNo = index * sheetSize;
				int endNo = Math.min(startNo + sheetSize, listSize);
				// 写入各条记录,每条记录对应excel表中的一行
				for (int i = startNo; i < endNo; i++) {
					row = sheet.createRow(i + 1 - startNo);
					T vo = (T) list.get(i); // 得到导出对象.
					for (int j = 0; j < fields.size(); j++) {
						// 获得field
						Field field = fields.get(j);
						// 设置实体类私有属性可访问
						field.setAccessible(true);
						ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
						int col = j;
						// 根据指定的顺序获得列号
						if (StringUtils.isNotBlank(attr.column())) {
							col = getExcelCol(attr.column());
						}
						// 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
						if (attr.isExport()) {
							// 创建cell
							cell = row.createCell(col);
							if (attr.isMark()) {
								newFont.setColor(HSSFFont.COLOR_RED); // 字体颜色
								newCellStyle.setFont(newFont);
								cell.setCellStyle(newCellStyle);
							} else {
								font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
								cellStyle.setFont(font);
								cell.setCellStyle(cellStyle);
							}
							// 如果数据存在就填入,不存在填入空格
							Class<?> classType = (Class<?>) field.getType();
							String value = null;
							if (field.get(vo) != null && classType.isAssignableFrom(Date.class)) {
								// 关于时间 最好格式化一下
								value = (String) field.get(vo);
							}
							cell.setCellValue(
									field.get(vo) == null ? "" : value == null ? String.valueOf(field.get(vo)) : value);
						}
					}
				}
				/* *************创建合计列*************** */
				XSSFRow lastRow = sheet.createRow((short) (sheet.getLastRowNum() + 1));
				for (int i = 0; i < fields.size(); i++) {
					Field field = fields.get(i);
					ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
					if (attr.isSum()) {
						int col = i;
						// 根据指定的顺序获得列号
						if (StringUtils.isNotBlank(attr.column())) {
							col = getExcelCol(attr.column());
						}
						BigDecimal totalNumber = BigDecimal.ZERO;
						for (int j = 1, len = (sheet.getLastRowNum() - 1); j < len; j++) {
							XSSFRow hssfRow = sheet.getRow(j);
							if (hssfRow != null) {
								XSSFCell hssfCell = hssfRow.getCell(col);
								/*
								 * if (hssfCell != null &&
								 * hssfCell.getCellType() ==
								 * HSSFCell.CELL_TYPE_STRING &&
								 * ValidateUtil.isFloat(hssfCell.
								 * getStringCellValue())) { totalNumber =
								 * BigDecimalUtils.getValue(totalNumber,
								 * BigDecimal.valueOf(Double.valueOf(hssfCell.
								 * getStringCellValue())), CalculateType.Add); }
								 */
							}
						}
						XSSFCell sumCell = lastRow.createCell(col);
						sumCell.setCellValue(new HSSFRichTextString("合计：" + totalNumber));
					}
				}
			}
			String classPath = Excel2007Util.class.getClassLoader().getResource("").getPath() + "templet/temp/";
			// 第1步、使用File类找到一个文件
			fileName = UUIDUtils.getUUID() + "_" + fileName + ".xlsx";
			File file = new File(classPath + fileName); // 声明File对象
			// 第2步、通过子类实例化父类对象
			OutputStream out = null; // 准备好一个输出的对象
			out = new FileOutputStream(file); // 通过对象多态性，进行实例化
			// 第3步、进行写操作
			// 文件不存在会自动创建
			out.flush();
			workbook.write(out);
			// 第4步、关闭输出流
			out.close();
			String filePath = classPath + fileName;
			return filePath;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcelException("将list数据源的数据导入到excel表单异常!");
		}

	}

	/**
	 * 
	 * @param response
	 * @param list
	 * @param sheetName
	 * @param fileName
	 * @throws ExcelException
	 */
	public void writeToPage(HttpServletResponse response, List<T> list, String sheetName, String fileName)
			throws ExcelException {
		OutputStream out = null;
		try {
			String filePath = this.getListToExcel(list, sheetName, fileName);
			// filePath =
			// "E:\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\benefitFarmerLoan-admin\\WEB-INF\\classes\\8cce18a747004362a8709d1950f75a9floanInfo.xlsx";
			File file = new File(filePath);
			if (file == null || !file.exists()) {
				return;
			}
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
			out = response.getOutputStream();
			out.write(FileUtils.readFileToByteArray(file));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					new ExcelException("表格导出错误......");
				}
			}
		}

	}

	/**
	 * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
	 *
	 * @param col
	 */
	public static int getExcelCol(String col) {
		col = col.toUpperCase();
		// 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
		int count = -1;
		char[] cs = col.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
		}
		return count;
	}

	/**
	 * 设置单元格上提示
	 *
	 * @param sheet
	 *            要设置的sheet.
	 * @param promptTitle
	 *            标题
	 * @param promptContent
	 *            内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static XSSFSheet setHSSFPrompt(XSSFSheet sheet, String promptTitle, String promptContent, int firstRow,
			int endRow, int firstCol, int endCol) {
		// 构造constraint对象
		DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");
		// 四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
		data_validation_view.createPromptBox(promptTitle, promptContent);
		sheet.addValidationData(data_validation_view);
		return sheet;
	}

	/**
	 * 设置某些列的值只能输入预制的数据,显示下拉框.
	 *
	 * @param sheet
	 *            要设置的sheet.
	 * @param textlist
	 *            下拉框显示的内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static XSSFSheet setHSSFValidation(XSSFSheet sheet, String[] textlist, int firstRow, int endRow,
			int firstCol, int endCol) {
		// 加载下拉列表内容
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
		sheet.addValidationData(data_validation_list);
		return sheet;
	}

	public static void main(String[] a) {

		try {
			List<BatchExport> list = Lists.<BatchExport> newArrayList();
			BatchExport batchExport = new BatchExport();
			batchExport.setIdCard("22222222222");
			batchExport.setRealName("哈哈哈哈");
			list.add(batchExport);

			String patch = new Excel2007Util<BatchExport>(BatchExport.class).getListToExcel(list, "测试", "测试");
			logger.info("保存路径是：{}",patch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}