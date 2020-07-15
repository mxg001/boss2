package cn.eeepay.framework.serviceImpl.exportBigData;

import cn.eeepay.framework.util.FileUtil;
import cn.eeepay.framework.util.RandomNumber;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ExportBase {

    public static String FILENAME="exportFile";

    public static String FILE_URL = "IMAGES_URL";

    /**
     * 根文件目录的创建
     * @param baseFolder 数据字典根目录
     * @return 文件存储母目录
     */
    public static String fileBase(String baseFolder){
        //母文件
        File baseNameFile = new File(baseFolder);
        FileUtil.createFolder(baseNameFile,0);
        //下载文件目录
        String randomStr= RandomNumber.mumberRandom("file",20,4);
        String exportFolder=baseFolder+File.separator+randomStr;// D:\image\exportFile\aaa1233
        File exportFolderFile = new File(exportFolder);
        FileUtil.createFolder(exportFolderFile,0);
        return exportFolder;
    }


    /**
     * 打包成zip文件，清楚临时文件
     * @param baseFolder 根目录
     * @param exportFolder 需要打包的文件目录
     * @param identificationStr zip分类文件夹名
     * @param zipName zip文件名
     * @return zip文件名
     * @throws IOException
     */
    public static void zipDelete(String baseFolder,String exportFolder,String identificationStr,String zipName) throws IOException{
        //zip文件夹名
        String identificationStrs=baseFolder+ File.separator+identificationStr;//D:\image\exportFile\zip_1122.zip
        File identificationStrFile = new File(identificationStrs);
        FileUtil.createFolder(identificationStrFile,0);
        //zip文件名
        String zips=identificationStrs+ File.separator+zipName;//D:\image\exportFile\zip_1122.zip
        File zipsFile = new File(zips);
        FileUtil.createFolder(zipsFile,1);
        //zip打包
        FileUtil.zipsFile(new File(exportFolder),zipsFile);
        //删除临时文件
        FileUtil.delFolder(exportFolder);
    }


    public static int writeData(SXSSFWorkbook wb,Sheet sheet,int rowIdx,List<Map<String, String>> data,String[] cols) throws IOException {
        Object cellValue;
        CellStyle bodyStyle = createBodyStyle(wb);
        for (int i = 0; i < data.size(); i++) {
            // 创建行
            rowIdx=++rowIdx;
            Row row = sheet.createRow(rowIdx);
            for (short j = 0; j < cols.length; j++) {
                Cell cell = row.createCell(j);
                // 定义单元格为字符串类型,不设置默认为“常规”
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellStyle(bodyStyle);
                cellValue = data.get(i).get(cols[j]);
                cell.setCellValue(cellValue == null ? "" : cellValue.toString());
            }
        }
        return rowIdx;
    }

    /**
     * 构建表格表头
     *
     * @throws IOException
     */
    public static void builTitle(SXSSFWorkbook wb,Sheet sheet,double[] cellWidth,int rowIdx,String[] colsName) throws IOException {
        Cell cell = null;
        Row row = null;
        int cols = colsName.length;
        CellStyle headStyle = createHeaderStyle(wb);
        row = sheet.createRow(rowIdx);
        row.setHeightInPoints((short) 25);
        for (short i = 0; i < cols; i++) {
            double _cellWidth  = 6000 ;
            if (cellWidth != null) {
                _cellWidth = cellWidth[i];
            }
            sheet.setColumnWidth(i, (short) _cellWidth);
            cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            // 定义单元格为字符串类型,不设置默认为“常规”
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(colsName[i]);
        }
    }
    /**
     * 构建表格列头样式
     *
     * @param wb
     * @return
     */
    private static CellStyle createHeaderStyle(Workbook wb) {
        // 设置字体
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 12); // 字体高度
        font.setColor(Font.COLOR_NORMAL); // 字体颜色
        font.setFontName("黑体"); // 字体
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度
        // 设置单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 水平布局：居中
        // 边框
        cellStyle.setBorderTop((short) 1);
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    /**
     * 构建表格列样式
     *
     * @param wb
     * @return
     */
    private static CellStyle createBodyStyle(Workbook wb) {
        // 设置单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT); // 水平布局：居中
        // 边框
        cellStyle.setBorderTop((short) 1);
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setWrapText(true);
        return cellStyle;
    }
}
