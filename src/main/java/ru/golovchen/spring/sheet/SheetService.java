package ru.golovchen.spring.sheet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class SheetService {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    public static final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    List<HouseRow> getHouses() {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            final String range = "Дома!A2:W1000";
            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            ValueRange response = service.spreadsheets().values()
                    .get(SPREADSHEET_ID, range)
                    .execute();
            List<List<Object>> rows = response.getValues();
            if (rows == null || rows.isEmpty()) {
                return Collections.emptyList();
            } else {
                List<HouseRow> result = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    final List<Object> columns = rows.get(i);
                    try {
                        final HouseRow house = new HouseRow(i + 2);
                        house.link = parseString(0, columns);
                        house.price = parseDouble(3, columns);
                        house.landArea = parseDouble(5, columns);
                        house.commuteTime = parseDouble(6, columns);
                        house.houseArea = parseDouble(7, columns);
                        house.floors = parseInteger(8, columns);
                        house.landStatus = parseString(9, columns);
                        house.wallMaterial =  parseString(10, columns);
                        house.floorMaterial =  parseString(11, columns);
                        house.foundation =  parseString(12, columns);
                        house.year = parseInteger(13, columns);
                        house.finishing =  parseString(14, columns);
                        house.gas =  parseString(15, columns);
                        house.water =  parseString(16, columns);
                        house.sewerage =  parseString(17, columns);
                        house.internet =  parseString(18, columns);
                        house.garage = parseString(20, columns);
                        result.add(house);
                    } catch (RuntimeException e) {
                        throw new RuntimeException("Unable to parse row " + i + " " + columns, e);
                    }
                }
                return result;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String parseString(int columnNumber, List<Object> columns) {
        final Object obj = getIfNotEmpty(columnNumber, columns);
        if (obj == null) {
            return null;
        }
        return (String)obj;
    }


    private Double parseDouble(int columnNumber, List<Object> columns) {
        final Object obj = getIfNotEmpty(columnNumber, columns);
        if (obj == null) {
            return null;
        }
        return Double.parseDouble((String)obj);
    }


    private Integer parseInteger(int columnNumber, List<Object> columns) {
        final Object obj = getIfNotEmpty(columnNumber, columns);
        if (obj == null) {
            return null;
        }
        return Integer.parseInt((String)obj);
    }

    private Object getIfNotEmpty(int columnNumber, List<Object> columns) {
        if (columns.size() <= columnNumber) {
            return null;
        }
        final Object obj = columns.get(columnNumber);
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            if (((String) obj).isEmpty()) {
                return null;
            }
        }
        return obj;
    }
}
