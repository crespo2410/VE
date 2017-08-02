package Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.Activity.RefuelingExpensesDetail;
import com.example.crespo.vehicleexpenses.Activity.UpdateRefuelingExpense;
import com.example.crespo.vehicleexpenses.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Adapters.RefuellingAdapter;
import Adapters.ServicesAdapter;
import Interfaces.CardItemClickListener;
import Models.ParametersModel;
import Models.TocenjeModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment klasa - prikaz liste Tocenja goriva
 */
public class RefuelingListaFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String TAG = "PdfCreatorActivity3";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 114;
    private RecyclerView recyclerView;
    private RefuellingAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TocenjeModel> lista_tocenja_api;
    private FloatingActionButton button;
    private int vozilo_id;
    private TextView empty;
    private File pdfFile;



    public RefuelingListaFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());
        vozilo_id = manager.getVehicleId();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tocenje_lista, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (savedInstanceState != null) {
            lista_tocenja_api = savedInstanceState.getParcelableArrayList("ListaTocenjaBundle");
        }

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewTocenja);
        button = (FloatingActionButton) getView().findViewById(R.id.buttonAddGas);
        button.setImageResource(R.drawable.ic_note_add_white_48dp);


        empty = (TextView) getActivity().findViewById(R.id.emptyTocenjeList);


        postaviListu(savedInstanceState);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getActivity(), UpdateRefuelingExpense.class);
                intent.putExtra("idButtona", button.getId());
                startActivity(intent);


            }
        });


    }





    private void postaviListu(Bundle savedInstanceState){

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());

        ParametersModel s = new ParametersModel();
        s.setVozilo_id(manager.getVehicleId());

        if(savedInstanceState == null) {

            Call<ArrayList<TocenjeModel>> getTocenjeDataCall = RestClient.getInstance().getApiService().getTocenja(s);


            getTocenjeDataCall.enqueue(new Callback<ArrayList<TocenjeModel>>() {
                @Override
                public void onResponse(Call<ArrayList<TocenjeModel>> call, Response<ArrayList<TocenjeModel>> response) {

                    if (response.isSuccessful()) {

                        lista_tocenja_api = response.body();
                        adapter = new RefuellingAdapter(getContext(), lista_tocenja_api);
                        adapter.setClickListenerforRecycler(new CardItemClickListener() {
                            @Override
                            public void itemClicked(View view, int position) {
                                TocenjeModel modelSlanje = lista_tocenja_api.get(position);

                                Bundle bundle = new Bundle();
                                bundle.putString("pozicija", lista_tocenja_api.get(position).getId());
                                bundle.putParcelable("ObjToDisplay", modelSlanje);

                                Intent intent = new Intent(getActivity(), RefuelingExpensesDetail.class);
                                intent.putExtra("bundle_lista", bundle);

                                startActivity(intent);

                            }
                        });


                        recyclerView.setAdapter(adapter);
                        emptyCheck();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TocenjeModel>> call, Throwable t) {


                    Log.d("ErrRetro_TocenjeLista", t.getMessage());
                    Toast.makeText(getActivity(), getString(R.string.poruka_greske_dohvata), Toast.LENGTH_SHORT).show();


                }
            });


            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

        }else {

            adapter = new RefuellingAdapter(getContext(), lista_tocenja_api);
            adapter.setClickListenerforRecycler(new CardItemClickListener() {
                @Override
                public void itemClicked(View view, int position) {
                    TocenjeModel modelSlanje = lista_tocenja_api.get(position);

                    Bundle bundle = new Bundle();
                    bundle.putString("pozicija", lista_tocenja_api.get(position).getId());
                    bundle.putParcelable("ObjToDisplay", modelSlanje);

                    Intent intent = new Intent(getActivity(), RefuelingExpensesDetail.class);
                    intent.putExtra("bundle_lista", bundle);

                    startActivity(intent);

                }
            });

            recyclerView.setAdapter(adapter);
            emptyCheck();

            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

        }


    }

    private void emptyCheck() {
        if (lista_tocenja_api.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_icon1:
                //Tu ide aktivnost za csv
                createSimpleReport();
            default:
                return super.onOptionsItemSelected(item);
        }

    }









    // METODA ZA SPREMANJE U PDF - FORMAT


    private void createSimpleReport() {

        try {

            createPdfWrapper();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void createPdfWrapper() throws FileNotFoundException, DocumentException, IOException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel(getString(R.string.dopustenje_memorija_poruka),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), getString(R.string.mem_err_pdf), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton(getString(R.string.potvrdi), okListener)
                .setNegativeButton(getString(R.string.odustani), null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException, IOException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();

        }

        pdfFile = new File(docsFolder.getAbsolutePath(), "RefuelingExpences.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();


        Drawable d = getResources().getDrawable(R.drawable.applogo);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.setAlignment(Element.ALIGN_LEFT);
        image.scaleAbsolute(150, 100);
        document.add(image);


        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettingsDetail(getContext());

        Calendar calendar = Calendar.getInstance();
        String date_time = String.format(Locale.getDefault(), "%1$td-%1$tm-%1$tY, %1$tH:%1tm:%1$tS ", calendar);


        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLDITALIC);
        Paragraph title = new Paragraph(getString(R.string.izvjestaj_punjenja) + date_time, chapterFont);
        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(8);
        PdfPCell cell;


        cell = new PdfPCell(new Phrase(getString(R.string.broj_tocenja)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.datum_tocenja)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.vrijeme_tocenja)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.razlog)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.notes_title)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(getString(R.string.na_km)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.naziv_benzinske)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.tocenje_cijena_hint)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        Double suma = 0.0;
        Double suma2 = 0.0;



        for (int i = 0; i < lista_tocenja_api.size(); i++) {


            settingValue.readDateFromSettings(getContext(),lista_tocenja_api.get(i).getTocenjeDatum(),lista_tocenja_api.get(i).getTocenjeVrijeme());


            cell = new PdfPCell(new Phrase(getString(R.string.tocenje_) + (i + 1)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(settingValue.getFormatted_date()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(settingValue.getVrijeme()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_tocenja_api.get(i).getRazlogNaziv()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_tocenja_api.get(i).getBiljeske()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(lista_tocenja_api.get(i).getKmTrenutno() + settingValue.getUnit_kmOrMile()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_tocenja_api.get(i).getBenzinskaNaziv()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(lista_tocenja_api.get(i).getUkupniTrosak() + settingValue.getCurrency_format()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            suma += Double.valueOf(lista_tocenja_api.get(i).getUkupniTrosak());
            suma2 += Double.valueOf(lista_tocenja_api.get(i).getUkupniTrosak());



            suma = 0.0;


        }

        table.setHeaderRows(1);


        cell = new PdfPCell(new Phrase(getString(R.string.sveukupno) + suma2.toString() + settingValue.getCurrency_format()));
        cell.setColspan(8);
        //cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        table.setWidthPercentage(100);

        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));


        document.add(new Paragraph(getString(R.string.napravljeno_ukupno) + lista_tocenja_api.size()  +getString(R.string._tocenja)));
        document.add(new Paragraph(getString(R.string.potroseno_je) + " "+ suma2 + settingValue.getCurrency_format()));


        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));


        Paragraph p = new Paragraph(getString(R.string.potpis));
        p.add(new LineSeparator());
        document.add(p);

        document.close();
        previewPdf();

    }

    private void previewPdf() {


        PackageManager packageManager = getActivity().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        } else {
            Toast.makeText(getContext(), R.string.preglednik_pdf_upozorenje, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ListaTocenjaBundle",  lista_tocenja_api);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);

    }
}
