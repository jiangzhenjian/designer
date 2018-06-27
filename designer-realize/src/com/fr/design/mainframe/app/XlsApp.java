package com.fr.design.mainframe.app;

import com.fr.base.extension.FileExtension;
import com.fr.file.FILE;
import com.fr.io.importer.ExcelReportImporter;
import com.fr.log.FineLoggerFactory;
import com.fr.main.impl.WorkBook;

/**
 * Created by juhaoyu on 2018/6/27.
 */
class XlsApp extends AbstractWorkBookApp {
    
    @Override
    public String[] defaultExtensions() {
        
        return new String[]{FileExtension.XLS.getExtension()};
    }
    
    @Override
    public WorkBook asIOFile(FILE tplFile) {
        
        WorkBook workbook = null;
        try {
            workbook = new ExcelReportImporter().generateWorkBookByStream(tplFile.asInputStream());
        } catch (Exception exp) {
            FineLoggerFactory.getLogger().error("Failed to generate xls from " + tplFile, exp);
        }
        return workbook;
    }
}
