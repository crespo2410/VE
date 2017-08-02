package Fragments;


import android.Manifest;
import android.app.AlertDialog;
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

import com.example.crespo.vehicleexpenses.Activity.ServiceExpensesDetail;
import com.example.crespo.vehicleexpenses.Activity.UpdateServiceExpense;
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
import java.util.List;
import java.util.Locale;

import Adapters.ServicesAdapter;
import Interfaces.CardItemClickListener;
import Models.ParametersModel;
import Models.ServiceExpensesModel;
import Models.ServiceModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment koji radi na istom principu kao i OtherExpensesListaFragment - pogledaj ga za detalje, samo
 * ovaj fragment jest za stvari vezane uz listu servisa
 */
public class ServisiListaFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private RecyclerView recyclerView;
    private ServicesAdapter servicesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ServiceModel> lista_servisi_api;
    private ArrayList<ServiceExpensesModel> lista_troskova_api;
    private FloatingActionButton button;
    private TextView empty;
    private File pdfFile;
    private int vozilo_id;


    public ServisiListaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_servisi_lista, container, false);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ListaServisaBundle", (ArrayList<? extends Parcelable>) lista_servisi_api);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
           lista_servisi_api = savedInstanceState.getParcelableArrayList("ListaServisaBundle");
        }




        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewServisi);
        button = (FloatingActionButton) getView().findViewById(R.id.buttonAddService);
        button.setImageResource(R.drawable.ic_note_add_white_48dp);

        empty = (TextView) getActivity().findViewById(R.id.emptyServiseList);

        postaviListu(savedInstanceState);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), UpdateServiceExpense.class);
                intent.putExtra("idButtona", button.getId());
                startActivity(intent);

            }
        });


    }

    private void postaviListu(Bundle saveInstanceState) {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());


        if(saveInstanceState == null) {

            ParametersModel s = new ParametersModel();
            s.setVozilo_id(manager.getVehicleId());

            Call<List<ServiceModel>> getServisiCall = RestClient.getInstance().getApiService().getServise(s);

            getServisiCall.enqueue(new Callback<List<ServiceModel>>() {
                @Override
                public void onResponse(Call<List<ServiceModel>> call, Response<List<ServiceModel>> response) {

                    if (response.isSuccessful()) {

                        lista_servisi_api = response.body();
                        servicesAdapter = new ServicesAdapter(getContext(), lista_servisi_api);
                        servicesAdapter.setClickListenerforRecycler(new CardItemClickListener() {
                            @Override
                            public void itemClicked(View view, int position) {

                                ServiceModel serviceModel = lista_servisi_api.get(position);

                                Bundle bundle = new Bundle();
                                bundle.putString("pozicija", lista_servisi_api.get(position).getId());
                                bundle.putParcelable("ObjToDisplay", serviceModel);

                                Intent intent = new Intent(getActivity(), ServiceExpensesDetail.class);
                                intent.putExtra("bundle_lista", bundle);

                                startActivity(intent);

                            }
                        });

                        recyclerView.setAdapter(servicesAdapter);
                        emptyCheck();

                    }

                }

                @Override
                public void onFailure(Call<List<ServiceModel>> call, Throwable t) {
                    Log.d("ErrServisiLista", t.getMessage());
                    Toast.makeText(getActivity(), "Problemi s dohvatom podataka!", Toast.LENGTH_SHORT).show();


                }
            });


            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }else {

            servicesAdapter = new ServicesAdapter(getContext(), lista_servisi_api);
            servicesAdapter.setClickListenerforRecycler(new CardItemClickListener() {
                @Override
                public void itemClicked(View view, int position) {

                    ServiceModel serviceModel = lista_servisi_api.get(position);

                    Bundle bundle = new Bundle();
                    bundle.putString("pozicija", lista_servisi_api.get(position).getId());
                    bundle.putParcelable("ObjToDisplay", serviceModel);

                    Intent intent = new Intent(getActivity(), ServiceExpensesDetail.class);
                    intent.putExtra("bundle_lista", bundle);

                    startActivity(intent);

                }
            });

            recyclerView.setAdapter(servicesAdapter);
            emptyCheck();

            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);


        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_icon1:

                createSimpleReport();

            default:
                return super.onOptionsItemSelected(item);


        }

    }





    private void emptyCheck() {
        if (lista_servisi_api.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }






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
                    Toast.makeText(getContext(),getString(R.string.mem_err_pdf), Toast.LENGTH_SHORT)
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
                .setNegativeButton(R.string.odustani, null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException, IOException {

        //prvo se određuje direktorij u kojem će se nalaziti dokument
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();

        }

        //potom se u dotičnom direktoriju stvara novi file s pripadajućim nazivom
        //stvaranje outputStreama, dokument objekta i njegovo otvaranje
        pdfFile = new File(docsFolder.getAbsolutePath(), "ServiceExpences.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        //definiranje slike u zaglavlju
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
        Paragraph title = new Paragraph(getString(R.string.izvjestaj_pdf_title) + date_time, chapterFont);
        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(8);
        PdfPCell cell;


        cell = new PdfPCell(new Phrase(getString(R.string.broj_servisa_title)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.datum_servisa_title)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.vrijeme_servisa_title)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.obavljeno_kod_title)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.notes_title)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(getString(R.string.na_km)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.naziv_servisa)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.tocenje_cijena_hint)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        Double suma = 0.0;
        Double suma2 = 0.0;


        for (int i = 0; i < lista_servisi_api.size(); i++) {

            lista_troskova_api = lista_servisi_api.get(i).getTros();


            cell = new PdfPCell(new Phrase(getString(R.string.servis) + (i + 1)));
            cell.setRowspan(lista_troskova_api.size());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            settingValue.readDateFromSettings(getContext(),lista_servisi_api.get(i).getDatumServis(),lista_servisi_api.get(i).getVrijemeServis());

            cell = new PdfPCell(new Phrase(settingValue.getFormatted_date()));
            cell.setRowspan(lista_troskova_api.size());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(settingValue.getVrijeme()));
            cell.setRowspan(lista_troskova_api.size());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_servisi_api.get(i).getNazivObrtnika()));
            cell.setRowspan(lista_troskova_api.size());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_servisi_api.get(i).getBiljeske()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(lista_troskova_api.size());
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(lista_servisi_api.get(i).getKmTrenutno() + settingValue.getUnit_kmOrMile()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(lista_troskova_api.size());
            table.addCell(cell);


            for (int j = 0; j < lista_troskova_api.size(); j++) {


                cell = new PdfPCell(new Phrase(lista_troskova_api.get(j).getNazivServisa()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(lista_troskova_api.get(j).getIznos()  + settingValue.getCurrency_format()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                suma += Double.valueOf(lista_troskova_api.get(j).getIznos());
                suma2 += Double.valueOf(lista_troskova_api.get(j).getIznos());


            }


            cell = new PdfPCell(new Phrase(" = " + suma.toString()  +settingValue.getCurrency_format()));
            cell.setColspan(8);
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);


            suma = 0.0;


        }

        table.setHeaderRows(1);


        cell = new PdfPCell(new Phrase(getString(R.string.sveukupno) + suma2.toString()  + settingValue.getCurrency_format()));
        cell.setColspan(8);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        table.setWidthPercentage(100);

        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));


        document.add(new Paragraph(getString(R.string.napravljeno_ukupno) + lista_servisi_api.size() + getString(R.string.ser)));
        document.add(new Paragraph(getString(R.string.potroseno_je) + suma2  + settingValue.getCurrency_format()));


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


