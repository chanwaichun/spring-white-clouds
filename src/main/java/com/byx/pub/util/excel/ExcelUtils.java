package com.byx.pub.util.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExcelUtils {

    public static void exportExcel(HttpServletRequest request, HttpServletResponse response, String fileName, ExcelData data) throws Exception {
        // 告诉浏览器用什么软件可以打开此文件
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        Browser browser = UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowser();
        if (browser != null) {
            if (browser == Browser.IE || browser == Browser.EDGE) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            } else if (browser == Browser.OPERA || browser == Browser.CHROME) {
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
            } else if (browser == Browser.SAFARI) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"");
            } else if (browser == Browser.FIREFOX) {
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
            } else {
                response.addHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes("gb2312"), "ISO-8859-1"));
            }
        }
        // 下载文件的默认名称
        exportExcel(data, response.getOutputStream());
    }

    public static void exportExcel(ExcelData data, OutputStream out) throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            String sheetName = data.getName();
            if (null == sheetName) {
                sheetName = "Sheet1";
            }
            XSSFSheet sheet = wb.createSheet(sheetName);
            writeExcel(wb, sheet, data);

            wb.write(out);
        } finally {
            wb.close();
        }
    }

    private static void writeExcel(XSSFWorkbook wb, Sheet sheet, ExcelData data) {

        int rowIndex = 0;

        rowIndex = writeTitlesToExcel(wb, sheet, data.getTitles());
        writeRowsToExcel(wb, sheet, data.getRows(), rowIndex);
        autoSizeColumns(sheet, data.getTitles().size() + 1);

    }

    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex = 0;

        Font titleFont = wb.createFont();
        titleFont.setFontName("simsun");
        titleFont.setBold(true);
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle titleStyle = wb.createCellStyle();
//        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setFillForegroundColor(new XSSFColor(new Color(182, 184, 192)));
//        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        titleStyle.setWrapText(true);
        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new Color(182, 184, 192)));

        Row titleRow = sheet.createRow(rowIndex);
        // titleRow.setHeightInPoints(25);
        colIndex = 0;

        for (String field : titles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }
        rowIndex++;
        return rowIndex;
    }

    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex) {
        int colIndex = 0;

        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        // dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setFont(dataFont);
        dataStyle.setWrapText(true);
        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));

        for (List<Object> rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            // dataRow.setHeightInPoints(25);
            colIndex = 0;

            for (Object cellData : rowData) {
                Cell cell = dataRow.createCell(colIndex);
                if (cellData != null) {
                    cell.setCellValue(cellData.toString());
                } else {
                    cell.setCellValue("");
                }

                cell.setCellStyle(dataStyle);
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    private static void autoSizeColumns(Sheet sheet, int columnNumber) {

        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (int) (sheet.getColumnWidth(i) + 100);
            if (newWidth > orgWidth) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }

    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
    }

    public static void exportExcelByAlibaba(HttpServletRequest request, HttpServletResponse response, String fileName, List<? extends BaseRowModel> data, Class model) throws Exception {
        fileName = URLEncoder.encode(fileName, "UTF8");
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        Browser browser = UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowser();
        if (browser != null) {
            if (browser == Browser.IE || browser == Browser.EDGE) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            } else if (browser == Browser.OPERA || browser == Browser.CHROME) {
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
            } else if (browser == Browser.SAFARI) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"");
            } else if (browser == Browser.FIREFOX) {
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + fileName);
            }
        }
        OutputStream out = response.getOutputStream();
        try {
            ExcelWriter writer = EasyExcelFactory.getWriter(out);
            com.alibaba.excel.metadata.Sheet sheet1 = new com.alibaba.excel.metadata.Sheet(1, 0, model);
            writer.write(data, sheet1);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定转换为类的字段数据格式读取数据，且采用默认数据位置读取
     * 即第一个sheet和第二行开始，默认第一行为表头且不读取
     *
     * @param inputStream 文件输入流
     * @param clazz       需要转换的类对象
     * @return 以clazz类对应格式的数据集合
     */
    public static <T> List<T> excelReadSync(InputStream inputStream, Class<T> clazz) {
        List<T> objects = EasyExcelFactory.read(inputStream).sheet().head(clazz).doReadSync();
        return objects;
    }

    public static List<Object> importExcel(InputStream inputStream) throws IOException {
        List<Object> datas = new ArrayList<Object>();
        ExcelReader excelReader = new ExcelReader(inputStream, null, new AnalysisEventListener() {
            @Override
            public void invoke(Object o, AnalysisContext analysisContext) {
                datas.add(o);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {

            }
        });
        excelReader.readAll();
        inputStream.close();

        return datas;
    }

    public static List<Object> importExcel(MultipartFile file) throws IOException {

        List<Object> datas = new ArrayList<Object>();
        InputStream inputStream = file.getInputStream();

        ExcelReader excelReader = new ExcelReader(inputStream, null, new AnalysisEventListener() {
            @Override
            public void invoke(Object o, AnalysisContext analysisContext) {
                datas.add(o);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {

            }
        });
        excelReader.readAll();
        inputStream.close();

        return datas;
    }


    public static File exportExcelFile(ExcelData data) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            String sheetName = data.getName();
            if (null == sheetName) {
                sheetName = "Sheet1";
            }
            XSSFSheet sheet = wb.createSheet(sheetName);

            writeExcel(wb, sheet, data);
            String id = String.valueOf(UUID.randomUUID());
            File file = new File(id + ".xlsx");

            OutputStream output = new FileOutputStream(file);

            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);

            wb.write(bufferedOutput);

            wb.close();
            return file;
        } finally {
            wb.close();
        }
    }


}
