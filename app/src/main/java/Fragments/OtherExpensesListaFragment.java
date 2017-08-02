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

import com.example.crespo.vehicleexpenses.Activity.OtherExpensesDetail;
import com.example.crespo.vehicleexpenses.Activity.UpdateOtherExpenses;
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

import Adapters.OtherExpensesAdapter;
import Adapters.RefuellingAdapter;
import Interfaces.CardItemClickListener;
import Models.ExpensesOtherExpensesModel;
import Models.OtherExpensesModel;
import Models.ParametersModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment klasa - prikaz liste Ostalih troskova
 */
public class OtherExpensesListaFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String TAG = "PdfCreatorActivity2";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 112;
    private RecyclerView recyclerView;
    private OtherExpensesAdapter otherExpensesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<OtherExpensesModel> lista_ostalih_troskova_api;
    private ArrayList<ExpensesOtherExpensesModel> lista_troskova_ostalihTroskova;
    private FloatingActionButton button;
    private TextView empty;
    private File pdfFile;



    public OtherExpensesListaFragment() {
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
        return inflater.inflate(R.layout.fragment_other_expenses_lista, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            lista_ostalih_troskova_api = savedInstanceState.getParcelableArrayList("ListaOstalihBundle");
        }

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewOtherExpenses);
        button = (FloatingActionButton) getView().findViewById(R.id.buttonAddOtherExpenses);
        button.setImageResource(R.drawable.ic_note_add_white_48dp);
        empty = (TextView) getActivity().findViewById(R.id.emptyOtherExpensesList);



        postaviListu(savedInstanceState);

        /* Button koji se nalazi ispod RecyclerViewa i koji pokreće novu "čistu" aktivnost
          UpdateOtherExpenses za razliku kada ju pokrećemo iz OtherExpenseDetailFragmenta kada
          je aktivnost UI popunjena vrijednostima.
          Zato se i šelje id buttona kako bi se znalo kako u Update djelu odreagirati */

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), UpdateOtherExpenses.class);
                intent.putExtra("idButtona", button.getId());
                startActivity(intent);


            }
        });


    }


    /**
     * Metoda koja postavlja listu tj. RecyclerView - dohvaća podatke sa servera i postavlja ih u RecyclerView
     * @param savedInstanceState
     */
    private void postaviListu(Bundle savedInstanceState) {

        //predaja parametara za http poziv kroz stvoreni objekt
        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());
        ParametersModel s = new ParametersModel();
        s.setVozilo_id(manager.getVehicleId());


        if(savedInstanceState == null) {
            //dohvat podataka s poslužitelja gdje se nalazi baza podataka
            Call<ArrayList<OtherExpensesModel>> ostaliTroskovi_repos = RestClient.getInstance().getApiService().getOstaleTroskove(s);

            ostaliTroskovi_repos.enqueue(new Callback<ArrayList<OtherExpensesModel>>() {
                @Override
                public void onResponse(Call<ArrayList<OtherExpensesModel>> call, Response<ArrayList<OtherExpensesModel>> response) {


                    if (response.isSuccessful()) {


                        lista_ostalih_troskova_api = response.body();
                        otherExpensesAdapter = new OtherExpensesAdapter(getContext(), lista_ostalih_troskova_api);
                        //postavljanje listenera za moguće otvaranje detalja kod pritiska na element
                        otherExpensesAdapter.setClickListenerforRecycler(new CardItemClickListener() {
                            @Override
                            public void itemClicked(View view, int position) {


                                OtherExpensesModel otherExpensesModel = lista_ostalih_troskova_api.get(position);

                                Bundle bundle = new Bundle();
                                bundle.putString("pozicija", lista_ostalih_troskova_api.get(position).getIdOt());
                                bundle.putParcelable("ObjToDisplay", otherExpensesModel);

                                Intent intent = new Intent(getActivity(), OtherExpensesDetail.class);
                                intent.putExtra("bundle_lista", bundle);

                                startActivity(intent);


                            }
                        });


                        recyclerView.setAdapter(otherExpensesAdapter);
                        emptyCheck();


                    }

                }

                @Override
                public void onFailure(Call<ArrayList<OtherExpensesModel>> call, Throwable t) {


                    Log.d("Err_OstaliTroskoviLista", t.getMessage());
                    Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();


                }
            });

            //dodavanje LayoutManagera
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }else{

            otherExpensesAdapter = new OtherExpensesAdapter(getContext(), lista_ostalih_troskova_api);
            otherExpensesAdapter.setClickListenerforRecycler(new CardItemClickListener() {
                @Override
                public void itemClicked(View view, int position) {


                    OtherExpensesModel otherExpensesModel = lista_ostalih_troskova_api.get(position);

                    Bundle bundle = new Bundle();
                    bundle.putString("pozicija", lista_ostalih_troskova_api.get(position).getIdOt());
                    bundle.putParcelable("ObjToDisplay", otherExpensesModel);

                    Intent intent = new Intent(getActivity(), OtherExpensesDetail.class);
                    intent.putExtra("bundle_lista", bundle);

                    startActivity(intent);


                }
            });

            recyclerView.setAdapter(otherExpensesAdapter);
            emptyCheck();

            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ListaOstalihBundle", (ArrayList<? extends Parcelable>) lista_ostalih_troskova_api);

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




    /**
     * Metoda za provjeru da li je lista dohvačenih podataka prazna kako bi se znalo
     * da li treba ispisati poruku u TextView da nema podataka
     */
    private void emptyCheck() {
        if (lista_ostalih_troskova_api.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }





    /**
     * metoda za kreiranje jednostavnog PDF dokumenta
     */
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


    /**
     * Metoda koja provjerava i rješava probleme uz dopuštenja pisanja po memoriji uređaja
     *
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws IOException
     */
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
                    Toast.makeText(getContext(), R.string.mem_err_pdf, Toast.LENGTH_SHORT)
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


    /**
     * Metoda stvaranja PDF-a i upotreba tabličnog prikaza
     *
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws IOException
     */
    private void createPdf() throws FileNotFoundException, DocumentException, IOException {

        //Direktorij u koji ćemo spremiti PDF
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        //ako ne postoji folder, stvori ga
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }

        pdfFile = new File(docsFolder.getAbsolutePath(), "OtherExpenses.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();


        //dodavalje slike u zaglavlje
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
        Paragraph title = new Paragraph(getString(R.string.izvjestaj_ot_pdf) + date_time, chapterFont);
        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(8);
        PdfPCell cell;


        cell = new PdfPCell(new Phrase(getString(R.string.broj_troska_pdf)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.datum_troska_pdf)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.vrijeme_t_pdf)));
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


        cell = new PdfPCell(new Phrase(getString(R.string.naziv_t)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(getString(R.string.tocenje_cijena_hint)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);


        Double suma = 0.0;
        Double suma2 = 0.0;


        for (int i = 0; i < lista_ostalih_troskova_api.size(); i++) {

            lista_troskova_ostalihTroskova = lista_ostalih_troskova_api.get(i).getTros();


            //dodavanje željenih vrijednosti prikaza u čelije

            cell = new PdfPCell(new Phrase(getString(R.string.ot_pdf) + (i + 1)));
            cell.setRowspan(lista_troskova_ostalihTroskova.size());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_ostalih_troskova_api.get(i).getDatumTroska().toString()));
            cell.setRowspan(lista_troskova_ostalihTroskova.size());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_ostalih_troskova_api.get(i).getVrijemeTroska()));
            cell.setRowspan(lista_troskova_ostalihTroskova.size());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_ostalih_troskova_api.get(i).getNazivObrtnika()));
            cell.setRowspan(lista_troskova_ostalihTroskova.size());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_ostalih_troskova_api.get(i).getBiljeske()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(lista_troskova_ostalihTroskova.size());
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lista_ostalih_troskova_api.get(i).getKmTrenutno() + settingValue.getUnit_kmOrMile()));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(lista_troskova_ostalihTroskova.size());
            table.addCell(cell);


            for (int j = 0; j < lista_troskova_ostalihTroskova.size(); j++) {


                cell = new PdfPCell(new Phrase(lista_troskova_ostalihTroskova.get(j).getNazivOstalogTroska()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(lista_troskova_ostalihTroskova.get(j).getIznos()  + settingValue.getCurrency_format()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                suma += Double.valueOf(lista_troskova_ostalihTroskova.get(j).getIznos());
                suma2 += Double.valueOf(lista_troskova_ostalihTroskova.get(j).getIznos());


            }


            cell = new PdfPCell(new Phrase(" = " + suma.toString()  + settingValue.getCurrency_format()));
            cell.setColspan(8);
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);


            suma = 0.0;


        }

        table.setHeaderRows(1);


        cell = new PdfPCell(new Phrase(getString(R.string.sveukupno) + suma2.toString() + settingValue.getCurrency_format()));
        cell.setColspan(8);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        table.setWidthPercentage(100);

        //dodavanje stvorene tablice u tablicu
        document.add(table);

        //prazni paragrafi
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));


        document.add(new Paragraph(getString(R.string.napravljeno_ukupno) + lista_ostalih_troskova_api.size() + getString(R.string.other_exp_pdf)));
        document.add(new Paragraph(getString(R.string.potroseno_je) + suma2 + settingValue.getCurrency_format()));


        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));


        //dodavanje linije za potpis
        Paragraph p = new Paragraph(getString(R.string.potpis));
        p.add(new LineSeparator());
        document.add(p);

        document.close();
        previewPdf();

    }

    /**
     * metoda za pregled PDFa, koja također javlja ako moramo pribaviti isti
     *
     */
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
