package com.ajie.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/*
 *  @Author 阿杰
 *  @create 2021年01月05日 15:24
 */
public class POIUtils {
    private static final String xls = "xls";
    private static final String xlsx = "xlsx";
    private static final String DATE_FORMAT = "yyyy/MM/dd";

    public static List<String[]> readExcel(MultipartFile file) throws IOException {
        //检查文件合法性
        checkFile(file);
        Workbook workbook = getWorkbook(file);
        //返回数组集合 将每一行作为一个数组 所有行作为一个集合返回
        List<String[]> list = new ArrayList<>();
        if (workbook != null) {          //getNumberOfSheets 获得工作表的个数
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获取当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获取当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获取当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行(标题)的所有数据
                for (int rowNum = firstRowNum+1; rowNum < lastRowNum; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获取当前行开始的列
                    short firstCellNum = row.getFirstCellNum();
                    //获取当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    //创建长度为当前行的列数的数组
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    //循环当前行
                    for (int cellNum = 0; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                        if (cells[cellNum] == null || cells[cellNum].equals("")) {
                            continue;
                        }
                    }
                    list.add(cells);
                }
            }
        }
        return list;
    }

    private static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //如果当前单元格内容为日期类型，则需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if (dataFormatString.equals("m/d/yy")) {
            cellValue = new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
            return cellValue;
        }
        //将数字作为字符串避免出现1读成1.0
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串类型
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //字符解析故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    /**
     * 检查文件合法性
     * @param file
     */
    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (file == null) {
            throw new FileNotFoundException("文件不存在");
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            throw new IOException(fileName + "该文件不是Excel文件");
        }
    }

    public static Workbook getWorkbook(MultipartFile file) {
        //获取文件名
        String fileName = file.getOriginalFilename();
        Workbook workbook = null;
        //获取excel文件的IO流
        try {
            InputStream is = file.getInputStream();
            //根据文件后缀名不同 获取不同的excel实现对象
            if (fileName.endsWith(xls)) {
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }
}
