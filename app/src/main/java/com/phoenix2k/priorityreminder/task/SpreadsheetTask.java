package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendCellsRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.phoenix2k.priorityreminder.ProjectsColumns;
import com.phoenix2k.priorityreminder.error.SpreadsheetException;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.utils.DataUtils;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pushpan on 08/02/17.
 */

public abstract class SpreadsheetTask extends BasicTask {
    public SpreadsheetTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Spreadsheet;
    }

    public List<List<Object>> readSheet(String range, String sheetId) {
        try {
            ValueRange response;
            response = ((Sheets) getService()).spreadsheets().values()
                    .get(sheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            return values;
        } catch (Exception e) {
            setLastError(e);
            LogUtils.printException(e);
        }
        return null;
    }

    public boolean updateSheet(String range, List<List<Object>> data, String sheetId ) {
        try {
            ValueRange oRange = new ValueRange();
            oRange.setRange(range);
            oRange.setValues(data);
            List<ValueRange> oList = new ArrayList<>();
            oList.add(oRange);

            BatchUpdateValuesRequest oRequest = new BatchUpdateValuesRequest();
            oRequest.setValueInputOption("RAW");
            oRequest.setData(oList);

            BatchUpdateValuesResponse response = ((Sheets) getService()).spreadsheets().values().batchUpdate(sheetId, oRequest).execute();
            return response.getTotalUpdatedRows() >= 0;
        } catch (Exception e) {
            setLastError(e);
            LogUtils.printException(e);
        }
        return false;
    }

//    public boolean update(String range){
//        ValueRange valueRange = new ValueRange();
//        try {
//            ((Sheets) getService()).spreadsheets().values().update(getSpreadsheetId(), range, valueRange)
//                    .setValueInputOption("RAW")
//                    .execute();
//        } catch (IOException e) {
//            setLastError(e);
//            LogUtils.printException(e);
//        }
//        return false;
//    }

//    public BatchUpdateSpreadsheetResponse appendWorksheet(String cellValues) throws Exception {
//        AppendCellsRequest appendRequest = new AppendCellsRequest();
//        appendRequest.setSheetId( mSheet.getProperties().getSheetId() );
//        appendRequest.setRows( getRowDataListForCellStrings(cellValues) );
//        appendRequest.setFields("userEnteredValue");
//
//        Request req = new Request();
//        req.setAppendCells( appendRequest );
//
//        return executeBatchRequest(req);
//    }
//
//    BatchUpdateSpreadsheetResponse executeBatchRequest(Request request) throws Exception {
//        List<Request> requests = new ArrayList<>();
//        requests.add( request );
//
//        BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest();
//        batchRequest.setRequests( requests );
//
//        try {
//            return   ((Sheets) getService()).spreadsheets().batchUpdate(getSpreadsheetId(), batchRequest).execute();
//        } catch (IOException e) {
//            throw new SpreadsheetException("Error updating Speadsheet");
//        }
//    }

    public String getDataSpreadsheetId(){
        return PreferenceHelper.getSavedDataFileId(getContext());
    }
    public String getProjectSpreadsheetId(){
        return PreferenceHelper.getSavedProjectFileId(getContext());
    }
}
