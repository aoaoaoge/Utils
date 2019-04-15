
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * EXCEL导出工具类
 */
public class ExcelUtil {

    /**
     * 通用列表导出excel方法，一般与NativeListQuery对象合并使用
     *
     * @param content
     *            列表的表体内容，一般是通过mybatis查询的结果集
     * @param headers
     *            列表的表头，类型为LinkedHashMap，key为对应content中需显示的列名，value为该列的表头中文名
     *
     * @author 万羽
     *
     */
    public static HSSFWorkbook exportExcelList(List<HashMap> content,
                                               LinkedHashMap<String, String> headers) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row;
        HSSFCell cell;
        // 构造表头
        int headerIndex = 0;
        if (headers != null && !headers.isEmpty()) {
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 表头字体需设置为粗体
            font.setColor(IndexedColors.WHITE.getIndex());// 字体颜色
            CellStyle hearderStyle = workbook.createCellStyle();
            hearderStyle.setFont(font);
            hearderStyle.setFillForegroundColor(IndexedColors.DARK_YELLOW
                    .getIndex());// 前景色
            hearderStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            // 设置边框
            hearderStyle.setBorderBottom(CellStyle.BORDER_THIN);
            hearderStyle.setBorderLeft(CellStyle.BORDER_THIN);
            hearderStyle.setBorderRight(CellStyle.BORDER_THIN);
            hearderStyle.setBorderTop(CellStyle.BORDER_THIN);
            row = sheet.createRow(0);
            for (String columnName : headers.values()) {
                cell = row.createCell(headerIndex);
                cell.setCellValue(columnName);
                cell.setCellStyle(hearderStyle);
                headerIndex++;
            }
        }
        // 构造内容
        int rowIndex = 1;
        CellStyle contentCellStyle = workbook.createCellStyle();
        contentCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        contentCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        contentCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        contentCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        contentCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
                .getIndex());
        contentCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        for (HashMap contentRow : content) {
            row = sheet.createRow(rowIndex);
            int columnIndex = 0;
            if (headers != null && !headers.isEmpty()) {
                for (String columnName : headers.keySet()) {
                    cell = row.createCell(columnIndex);
                    if (contentRow.get(columnName) != null) {
                        cell.setCellValue(contentRow.get(columnName).toString());
                    }
                    cell.setCellStyle(contentCellStyle);
                    columnIndex++;
                }
            } else {
                for (Object columnValue : contentRow.values()) {
                    cell = row.createCell(columnIndex);
                    cell.setCellValue(columnValue.toString());
                    cell.setCellStyle(contentCellStyle);
                    columnIndex++;
                }
            }
            rowIndex++;
        }
        // 自动调整sheet中所有列的宽度，根据每一列的文字长度
        for (int i = 0; i < headerIndex; i++) {
            sheet.autoSizeColumn((short) i);
        }
        return workbook;
    }

    /**
     * 该方法用于把传递过来的数据填充在EXCEL特定的单元格中
     *
     * @param templateStream
     *            EXCEL模板的文件流
     * @param params
     *            一个HashMap键值对，key为EXCEL需要填充数据的单元格的标记名，value为该单元格需要填充的数据值
     * @return 返回一个被填充数据后的工作簿
     */
    public static HSSFWorkbook doOutputExcel(InputStream templateStream,
                                             HashMap<String, String> params) {
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(templateStream);
            int iSheetNum = workbook.getNumberOfSheets();
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                for (int numSheets = 0; numSheets < iSheetNum; numSheets++) {
                    if (null != workbook.getSheetAt(numSheets)) {
                        HSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
                        int iRowNum = aSheet.getLastRowNum();
                        for (int rowNumOfSheet = 0; rowNumOfSheet <= iRowNum; rowNumOfSheet++) {
                            if (null != aSheet.getRow(rowNumOfSheet)) {
                                HSSFRow aRow = aSheet.getRow(rowNumOfSheet);
                                int iCellNum = aRow.getLastCellNum();
                                for (int cellNumOfRow = 0; cellNumOfRow < iCellNum; cellNumOfRow++) {
                                    if (null != aRow.getCell(cellNumOfRow)) {
                                        HSSFCell aCell = aRow
                                                .getCell(cellNumOfRow);
                                        String cellValue = aCell
                                                .getStringCellValue();
                                        if (cellValue.indexOf("#" + key + "#") != -1) {
                                            if ((String) params.get(key) != null
                                                    && !"".equals((String) params
                                                    .get(key))) {
                                                aCell.setCellValue(cellValue
                                                        .replaceAll(
                                                                "#" + key + "#",
                                                                (String) params
                                                                        .get(key)));
                                            } else {
                                                aCell.setCellValue(cellValue
                                                        .replaceAll("#" + key
                                                                + "#", ""));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            /*
             * 设置页脚
             */
            HSSFSheet st = workbook.getSheetAt(0);
            HSSFFooter foot = st.getFooter();
            foot.setCenter(HSSFFooter.page() + "/" + HSSFFooter.numPages());// 显示的为
            // 当前页/总页数
            // 如:1/3
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 该方法用于把传递过来的数据填充在EXCEL特定的单元格和列表中(适用于含有列表的模板)
     *
     * @param templatePath
     *            EXCEL模板存放的路径
     * @param params
     *            一个HashMap键值对，key为EXCEL需要填充数据的单元格的标记名，value为该单元格需要填充的数据值
     * @param list
     *            列表数据
     * @param startNum
     *            列表开始行数
     * @return 返回一个被填充数据后的工作簿
     * @throws IOException
     */
    public static HSSFWorkbook doOutputExcelHasList(InputStream templateStream,
                                                    HashMap<String, String> params, List list, int startNum)
            throws IOException {
        // 创建对Excel工作簿文件的引用
        HSSFWorkbook workbook = new HSSFWorkbook(templateStream);
        int iSheetNum = workbook.getNumberOfSheets();
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                for (int numSheets = 0; numSheets < iSheetNum; numSheets++) {
                    if (null != workbook.getSheetAt(numSheets)) {
                        HSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
                        int iRowNum = aSheet.getLastRowNum();
                        for (int rowNumOfSheet = 0; rowNumOfSheet <= iRowNum; rowNumOfSheet++) {
                            if (null != aSheet.getRow(rowNumOfSheet)) {
                                HSSFRow aRow = aSheet.getRow(rowNumOfSheet);
                                int iCellNum = aRow.getLastCellNum();
                                for (int cellNumOfRow = 0; cellNumOfRow < iCellNum; cellNumOfRow++) {
                                    if (null != aRow.getCell(cellNumOfRow)) {
                                        HSSFCell aCell = aRow
                                                .getCell(cellNumOfRow);
                                        String cellValue = "";
                                        if (aCell.getCellType() == 1) {
                                            cellValue = aCell
                                                    .getStringCellValue();
                                        }
                                        if (cellValue.indexOf("#" + key + "#") != -1) {
                                            if ((String) params.get(key) != null
                                                    && !"".equals((String) params
                                                    .get(key))) {
                                                aCell.setCellValue(cellValue
                                                        .replaceAll(
                                                                "#" + key + "#",
                                                                (String) params
                                                                        .get(key)));
                                            } else {
                                                aCell.setCellValue(cellValue
                                                        .replaceAll("#" + key
                                                                + "#", ""));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap mapValue = (LinkedHashMap) list.get(i);
            int rowNum = startNum + i;
            HSSFRow aRow = workbook.getSheetAt(0).getRow(rowNum);
            if (aRow == null)
                aRow = workbook.getSheetAt(0).createRow(rowNum);
            Iterator it2 = (Iterator) mapValue.entrySet().iterator();
            int cellNum = 0;
            while (it2.hasNext()) {
                Map.Entry e = (Map.Entry) it2.next();
                HSSFCell cell = aRow.getCell(cellNum);
                if (cell == null)
                    cell = aRow.createCell(cellNum);
                if (e.getKey() != null) {
                    if (e.getValue() != null) {
                        cell.setCellValue(e.getValue().toString());
                    }
                }
                cellNum++;
            }
        }
        return workbook;
    }


    public static HSSFWorkbook doOutputLatticeExcelList(InputStream templateStream,
                                                        HashMap<String, String> params, List list, int startNum,int k,boolean ka)
            throws IOException {
        // 创建对Excel工作簿文件的引用
        HSSFWorkbook workbook = new HSSFWorkbook(templateStream);
//		 HSSFSheet sheet = workbook.createSheet("Sheet12");//新建
        HSSFSheet sheet = workbook.getSheet("Sheet1");

        //设置合并的标题头(注意：横向合并的时候，标题头单元格必须长度和内容单元格一致否则合并时会把其他标题头单元格内容挤掉)
        if(k==0){
            if(ka){
//			        sheet.addMergedRegion(new CellRangeAddress(4,6,0,0));  //纵向： 合并第一列的第8行到第10行第
//			        sheet.addMergedRegion(new CellRangeAddress(4,6,16,16));//纵向：合并第14列的第8行到第10行第
//			        sheet.addMergedRegion(new CellRangeAddress(4,6,17,17));//纵向：合并第15列的第8行到第10行第
//			        sheet.addMergedRegion(new CellRangeAddress(4,6,18,18));//纵向：合并第16列的第8行到第10行第
//			        sheet.addMergedRegion(new CellRangeAddress(4,6,19,19));//纵向：合并第17列的第8行到第10行第
            }else{
//	        		 sheet.addMergedRegion(new CellRangeAddress(7,9,0,0));//纵向：合并第一列的第1行和第2行第
            }
        }else{
            if(!ka){
                for(int i=0;i<k-1;i++){
                    sheet.addMergedRegion(new CellRangeAddress(i*3+6,i*3+8,0,0));//纵向：合并第一列的第1行和第2行第
                    sheet.addMergedRegion(new CellRangeAddress(i*3+6,i*3+8,34,34));//横向：合并第一行的第2列到第4列
                    sheet.addMergedRegion(new CellRangeAddress(i*3+6,i*3+8,35,35));//横向：合并第一行的第2列到第4列
                    sheet.addMergedRegion(new CellRangeAddress(i*3+6,i*3+8,36,36));//横向：合并第一行的第2列到第4列
                    sheet.addMergedRegion(new CellRangeAddress(i*3+6,i*3+8,37,37));//横向：合并第一行的第2列到第4列
                }
            }else{
//	        		sheet.addMergedRegion(new CellRangeAddress(7,9,0,0));  //纵向： 合并第一列的第8行到第10行第
            }
        }
        //设置单元格风格，居中对齐.
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);//下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_DOTTED);//左边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框

        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap mapValue = (LinkedHashMap) list.get(i);
            int rowNum = startNum + i;
            HSSFRow aRow = workbook.getSheetAt(0).getRow(rowNum);
            if (aRow == null)
                aRow = workbook.getSheetAt(0).createRow(rowNum);
            Iterator it2 = (Iterator) mapValue.entrySet().iterator();
            int cellNum = 0;
            while (it2.hasNext()) {
                Map.Entry e = (Map.Entry) it2.next();
                HSSFCell cell = aRow.getCell(cellNum);
                if (cell == null)
                    cell = aRow.createCell(cellNum);
                if (e.getKey() != null) {
                    if (e.getValue() != null) {
                        cell.setCellValue(e.getValue().toString());
                    }
                }
                cellNum++;
            }
        }
        return workbook;
    }





    /**
     * 导出xls文件(合并单元格)
     * @param list
     * @param xlsFileName
     * @param sheetName
     */
    public static HSSFWorkbook reportMergeXls(InputStream templateStream,
                                              HashMap<String, String> params, List list, int startNum) throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook(templateStream);
        HSSFSheet sheet = wb.getSheet("监管人员核实");

        //设置单元格风格，居中对齐.
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        cs.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cs.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cs.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        //设置字体:
        HSSFFont font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);//设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示

        cs.setFont(font);//要用到的字体格式

        //sheet.setColumnWidth(0, 3766); //第一个参数代表列下标(从0开始),第2个参数代表宽度值
        //cs.setWrapText(true);//设置字体超出宽度自动换行

        //设置背景颜色
        //cs.setFillBackgroundColor(HSSFColor.BLUE.index);
        //cs.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);


        //创建第一行
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell ;



        //设置合并的标题头(注意：横向合并的时候，标题头单元格必须长度和内容单元格一致否则合并时会把其他标题头单元格内容挤掉)
        sheet.addMergedRegion(new CellRangeAddress(0,0,2,4));//横向：合并第一行的第2列到第4列
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//纵向：合并第一列的第1行和第2行第
        sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));//纵向：合并第二列的第1行和第2行第

        //设置对应的合并单元格标题
        row = sheet.createRow(1);
        for (int j = 2; j < 5; j++) {
            cell = row.createCell((short)j);
            cell.setCellStyle(cs);
            cell.setCellValue("value" + (j-1));
            sheet.autoSizeColumn(j);//自动设宽
        }
        //设置列值-内容
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 2);
            Map map = (Map) list.get(i);
            row.createCell(0).setCellValue("");
            row.createCell(1).setCellValue((String)map.get("name"));
            row.createCell(2).setCellValue((String)map.get("value1"));
            row.createCell(3).setCellValue((String)map.get("value2"));
            row.createCell(4).setCellValue((String)map.get("value3"));
        }

        return wb;
    }







    /**
     * 该方法用于把传递过来的数据填充在EXCEL特定的列表中(适用于含有多个列表的模板)
     *
     * @param workbook
     *            EXCEL模板对象
     * @param list
     *            列表数据
     * @param startNum
     *            列表开始行数
     * @return 返回一个被填充数据后的工作簿
     */
    public static HSSFWorkbook doOutputExcelMutilList(HSSFWorkbook workbook,
                                                      List list, int startNum) {
        try {
            for (int i = 0; i < list.size(); i++) {
                LinkedHashMap mapValue = (LinkedHashMap) list.get(i);
                int rowNum = startNum + i;
                HSSFRow aRow = workbook.getSheetAt(0).getRow(rowNum);
                if (aRow == null)
                    aRow = workbook.getSheetAt(0).createRow(rowNum);
                Iterator it2 = (Iterator) mapValue.entrySet().iterator();
                int cellNum = 0;
                while (it2.hasNext()) {
                    Map.Entry e = (Map.Entry) it2.next();
                    HSSFCell cell = aRow.getCell(cellNum);
                    if (cell == null)
                        cell = aRow.createCell(cellNum);
                    if (e.getKey() != null) {
                        if (e.getValue() != null) {
                            cell.setCellValue(e.getValue().toString());
                        }
                    }
                    cellNum++;
                }
            }
            return workbook;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
