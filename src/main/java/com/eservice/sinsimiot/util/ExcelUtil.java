package com.eservice.sinsimiot.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : silent
 * @description :  Excel 导入导出
 */
public class ExcelUtil {

    public static final String IMAGE_PNG = "png";
    public static final String IMAGE_JPG = "jpg";
    public static final String EXCEL_XLS = ".xls";
    public static final String EXCEL_XLSX = ".xlsx";

    /**
     * 一次导出最大量
     */
    static Integer dataSize = 100000;
    /**
     * 设置单元格宽度 相当于 18字符
     */
    static Integer width = 48 * 100;
    /**
     * 设置单元格高度 相当于 45 磅
     */
    static short headerHeight = 9 * 100;
    static short height = 10 * 100;
    /**
     * 设置字体大小
     */
    static short headerFontSize = 16;
    static short fontSize = 14;
    /**
     * 是否加粗
     */
    static boolean headerBold = true;
    static boolean bold = false;
    /**
     * 字体类型
     */
    static String fontStyle = "黑体";
    /**
     * 内间距
     */
    static Integer d = 1;

    /**
     * 导出之前先判断数据量大小
     */
    public static boolean existsFileSize(Integer Size) {
        if (Size > dataSize) {
            return false;
        }
        return true;
    }

    /**
     * 导出带图的Excel表格
     *
     * @param recordData  导出的数据（包含图片【传名称即可】）
     * @param excelHeader Excel 标题（名称与数据字段一一对应）
     * @param excelPath   Excel 文件存放路径
     * @param fileName    Excel 文件名称
     * @return
     */
    public static String insertDataInSheet(List<LinkedHashMap> recordData, String[] excelHeader, String excelPath, String fileName) throws IOException {
        if (recordData.size() > dataSize) {
            return null;
        }
        //放excel表格需要存放的路径
        if (!FileUtil.existsFile(excelPath)) {
            return "file create fail !";
        }
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet();
        FileOutputStream out = new FileOutputStream(excelPath + fileName);
        /**
         * 图片编辑器
         */
        Drawing patriarch = sheet.createDrawingPatriarch();
        //标题字体样式
        Font headerFont = workbook.createFont();
        headerFont.setFontName(fontStyle);
        headerFont.setFontHeightInPoints(headerFontSize);
        headerFont.setBold(headerBold);
        //正文字体样式
        Font font = workbook.createFont();
        font.setFontName(fontStyle);
        font.setFontHeightInPoints(fontSize);
        font.setBold(bold);
        //标题样式
        CellStyle headerStyle = workbook.createCellStyle();
        //设置字体样式
        headerStyle.setFont(headerFont);
        //设置水平对齐的样式为居中对齐;
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //正文样式
        CellStyle style = workbook.createCellStyle();
        //自动换行
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        //在表中存放查询到的数据放入对应的行号
        int rowNum = 0;
        //设置标题行，样式及内容
        Row row = sheet.createRow(rowNum++);
        row.setHeight(headerHeight);
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(excelHeader[i]);
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, width);
        }
        //设置正文，内容及样式
        for (Map<String, Object> record : recordData) {
            row = sheet.createRow(rowNum++);
            row.setHeight(height);
            //在表中存放查询到的数据放入对应的列号
            int cellNum = 0;
            for (String key : record.keySet()) {
                Cell cell = row.createCell(cellNum++);
                cell.setCellStyle(style);
                try {
                    if (key.contains("image")) {
                        File file = new File(record.get(key).toString());
                        BufferedImage bufferImg = ImageIO.read(file);
                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                        ImageIO.write(bufferImg, IMAGE_PNG, byteArrayOut);
                        /**
                         * 该构造函数有8个参数
                         * 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
                         * 后四个参数，前两个表示图片左上角所在的cellNum和 rowNum，后两个参数对应的表示图片右下角所在的cellNum和 rowNum，
                         * excel中的cellNum和rowNum的index都是从0开始的
                         */
                        HSSFClientAnchor anchor = new HSSFClientAnchor(d, d, d, d, (short) (cellNum - 1), (rowNum - 1), (short) cellNum, rowNum);
                        /**
                         MOVE_AND_RESIZE     随单元格改变
                         MOVE_DONT_RESIZE    不改变
                         DONT_MOVE_AND_RESIZE    不改变
                         */
                        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                        // 插入图片
                        patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    } else {
                        if (record.get(key) instanceof Integer) {
                            cell.setCellValue(((Integer) record.get(key)).doubleValue());
                        } else if (record.get(key) instanceof Float) {
                            cell.setCellValue(Double.valueOf(String.valueOf(record.get(key))));
                        } else {
                            if (record.get(key) != null) {
                                cell.setCellValue(record.get(key).toString());
                            } else {
                                cell.setCellValue("--");
                            }

                        }
                    }
                } catch (Exception e) {
                    cell.setCellValue("---");
                    e.printStackTrace();
                }
            }
        }
        workbook.write(out);
        out.close();
        return fileName;
    }

    /**
     * 导入
     *
     * @param file
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ArrayList<T> importRecord(File file, Class<T> clazz) throws Exception {
        ArrayList<T> dataJsonList = new ArrayList<>();
        ArrayList<String> fieldNames = new ArrayList<>();
        //获取导入字段名称
        for (Field field : clazz.getDeclaredFields()) {
            fieldNames.add(field.getName());
        }
        List<String[]> excel = getExcelData(file);
        if (excel != null && excel.size() > 0) {
            Map<String, Integer> fieldIndex = new HashMap<>();
            for (String fieldName : fieldNames) {
                String[] row = excel.get(0);
                for (int i = 0; i < row.length; i++) {
                    if (row[i].trim().equalsIgnoreCase(fieldName)) {
                        fieldIndex.put(fieldName, i);
                        break;
                    }
                }
            }
            for (int i = 1; i < excel.size(); i++) {
                String[] rowData = excel.get(i);
                StringBuffer dataJson = new StringBuffer("{");
                //json格式以{开始
                for (Map.Entry<String, Integer> entry : fieldIndex.entrySet()) {
                    dataJson.append("\"" + entry.getKey() + "\":\"" + rowData[entry.getValue()] + "\",");
                }
                //去除最后一个，json格式以}结束
                dataJsonList.add(JSONObject.parseObject(dataJson.substring(0, dataJson.lastIndexOf(",")) + "}", clazz));
            }
        }
        return dataJsonList;
    }

    /**
     * 读取Excel文件内容
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static List<String[]> getExcelData(File file) throws Exception {
        if (checkFile(file)) {
            //获得Workbook工作薄对象
            InputStream is = new FileInputStream(file);
            //创建Workbook工作薄对象，表示整个excel
            Workbook workbook = WorkbookFactory.create(is);
            //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
            List<String[]> list = new ArrayList<>();
            if (workbook != null) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(0);
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //有效列的个数
                int cellCount = 0;
                Row row = sheet.getRow(firstRowNum);
                if (row != null) {
                    //获得第一行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得第一行的结束列
                    int lastCellNum = row.getLastCellNum();
                    String[] cells = new String[lastCellNum];
                    //循环第一行，只统计连续不为空的标题，遇到空值就终止
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell).trim();
                        if (cells[cellNum] != null && !"".equalsIgnoreCase(cells[cellNum])) {
                            cellCount++;
                        } else {
                            break;
                        }
                    }
                    list.add(cells);
                }
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //统计每行空值的个数
                    int nullCount = 0;
                    //存储每行的数据
                    String[] cells = new String[cellCount];
                    //循环当前行
                    if (firstCellNum < 0) {
                        continue;
                    }
                    for (int cellNum = firstCellNum; cellNum < cellCount; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell).trim();
                        if (cells[cellNum].isEmpty()) {
                            nullCount++;
                        }
                    }
                    //如果当前行的空值列多余或者等与有效列的个数，则视为从当前行开始之后，没数据
                    if (nullCount >= cellCount) {
                        break;
                    } else {
                        list.add(cells);
                    }
                }
            }
            return list;
        }
        return null;
    }

    /**
     * 检查文件是否为Excel文件
     *
     * @param file
     * @return
     */
    private static boolean checkFile(File file) {
        //判断文件是否存在
        if (null == file) {
            return false;
        }
        //获得文件名
        String fileName = file.getName();
        //判断文件是否是excel文件
        if (!fileName.endsWith(EXCEL_XLS) && !fileName.endsWith(EXCEL_XLSX)) {
            return false;
        }
        return true;
    }

    /**
     * 检查单元格内容格式
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellTypeEnum()) {
            //数字
            case NUMERIC:
                cellValue = stringDateProcess(cell);
                break;
            //字符串
            case STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            //Boolean
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            //公式
            case FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK:
                //空值
                cellValue = "";
                break;
            //故障
            case ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


    /**
     * 日期格式处理
     *
     * @param cell
     * @return
     */
    private static String stringDateProcess(Cell cell) {
        String result = new String();
        // 处理日期格式、时间格式
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
            SimpleDateFormat sdf;
            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                sdf = new SimpleDateFormat("HH:mm");
            } else {// 日期
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            Date date = cell.getDateCellValue();
            result = sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 58) {
            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            double value = cell.getNumericCellValue();
            Date date = DateUtil.getJavaDate(value);
            result = sdf.format(date);
        } else {
            double value = cell.getNumericCellValue();
            CellStyle style = cell.getCellStyle();
            DecimalFormat format = new DecimalFormat();
            String temp = style.getDataFormatString();
            // 单元格设置成常规
            if ("General".equals(temp)) {
                format.applyPattern("#");
            }
            result = format.format(value);
        }
        return result;
    }

}
