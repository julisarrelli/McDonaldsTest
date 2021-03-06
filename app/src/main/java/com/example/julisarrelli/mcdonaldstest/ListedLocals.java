
package com.example.julisarrelli.mcdonaldstest;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.example.julisarrelli.mcdonaldstest.JavaClases.JSONParser;
        import com.example.julisarrelli.mcdonaldstest.JavaClases.Adapters.LocalsListViewAdapter;
        import com.example.julisarrelli.mcdonaldstest.JavaClases.Local;
        import com.example.julisarrelli.mcdonaldstest.JavaClases.Platform;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

        import static com.example.julisarrelli.mcdonaldstest.R.id.listView;

public class ListedLocals extends AppCompatActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    Platform platform=Platform.getInstance();

    ArrayList<HashMap<String, String>> usersList;


    // url to get all products list
    private static String url_getalllocals = "http://julisarrellidb.hol.es/mcconnect/getalllocals.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "locals";
    private static final String TAG_ID = "idlocal";
    private static final String TAG_ADRESS = "adress";
    private static final String TAG_CITY = "city";

    private ArrayList<String >adresses;
    private ArrayList<String> cities;
    private LocalsListViewAdapter adapter;


    // products JSONArray
    JSONArray products = null;

    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_locals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        list = (ListView) findViewById(listView);

        adresses=new ArrayList<String>();
        cities=new ArrayList<String>();

        // Cargar los productos en el Background Thread
        new LoadAllLocals().execute();
        Log.v("locales","puto");




        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(ListedLocals.this, ListedForms.class);
                startActivityForResult(intent, 0);

                HashMap<String,String> selectedItem = (HashMap<String, String>) parent.getItemAtPosition(position);
                String selectedLocal_Adress=selectedItem.get("adress");
                String selectedLocal_City=selectedItem.get("city");
                platform.setIdLocalToEvaluate(platform.getLocalId(selectedLocal_Adress,selectedLocal_City));

                //Toast.makeText(getApplicationContext(), String.valueOf(platform.getLocalId(selectedLocal_Adress,selectedLocal_City)),Toast.LENGTH_LONG).show();

                //Snackbar.make(parent,"Clickeaste el local"+position, Snackbar.LENGTH_SHORT).show();

            }
        });

    }


    class LoadAllLocals extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListedLocals.this);
            pDialog.setMessage("Cargando locales. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List params = new ArrayList();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_getalllocals, "GET", params);

            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String adress = c.getString(TAG_ADRESS);
                        String city = c.getString(TAG_CITY);



                        //cargamos el hashmap de la plataforma
                        Local local=new Local(Integer.parseInt(id),adress,city);
                        platform.addLocal(local);

                        //cargamos el vector para la list view
                        adresses.add(adress);
                        cities.add(city);



                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }



        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            cargarLista();
        }

    }

    private void cargarLista() {

        adapter=new LocalsListViewAdapter(this,adresses,cities);
        list.setAdapter(adapter);


    }

}
