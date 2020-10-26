package mx.edu.uaz.ingsoftware.poo2.main;

import junit.framework.TestCase;
import mx.edu.uaz.ingsoftware.poo2.clientes.basedatos.DaoConcursosREST;
import mx.edu.uaz.ingsoftware.poo2.entidades.Institucion;
import mx.edu.uaz.ingsoftware.poo2.interfaces.DAOConcursos;
import mx.edu.uaz.ingsoftware.poo2.utils.HttpUtils;
import org.dbunit.DefaultOperationListener;
import org.dbunit.IDatabaseTester;
import org.dbunit.IOperationListener;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServiciosREST extends TestCase {
    private static IDatabaseTester databaseTester;
    private final static String driverName="com.mysql.cj.jdbc.Driver";
    //private static String url="jdbc:mysql://localhost/controlconcursos";
    private static List<Institucion> datosEsperados;
    private static IDatabaseConnection conndbunit;
    private static int calif_inst;
    private static int calif_nombre;
    private final static int CALIF_OBTENER=5;
    private final static int CALIF_AGREGAR=10;
    private final static int CALIF_UPDATE=10;
    private final static int CALIF_DELETE=5;
    private final static int MAX_CALIF_INST=35;
    private final static double PORCENTAJE_INST=0.25;
    private static String urlbaseGF;
    private static String matricula;
    private static String nombreCompleto;
    private static String ubicacionGF;
    private static String puertoGF;
    private static DAOConcursos dao;

    public static class CustomConfigurationOperationListener extends DefaultOperationListener implements IOperationListener {
        @Override
        public void connectionRetrieved(IDatabaseConnection iDatabaseConnection) {
            super.connectionRetrieved(iDatabaseConnection);
            iDatabaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        }
    }

    @BeforeAll
    public static void inicializa() throws Exception {
        ResourceBundle res=ResourceBundle.getBundle("datosmysql", Locale.ROOT);
/*                new ResourceBundle.Control() {
                    @Override
                    public List<Locale> getCandidateLocales(String name,
                                                            Locale locale) {
                        return Collections.singletonList(Locale.ROOT);
                    }
                });*/
        String ubicacionmysl=res.getString("ubicacionmysql");
        String puertomysql=res.getString("puertomysql");
        String usuario=res.getString("usuario");
        String clave= res.getString("clave");
        String basedatos=res.getString("basedatos");
        matricula=res.getString("matricula");
        nombreCompleto=res.getString("nombre");
        ubicacionGF=res.getString("ubicacionglassfish");
        puertoGF=res.getString("puertoglassfish");
        urlbaseGF= String.format("http://%s:%s/RESTConcursos-%s",
                    ubicacionGF,puertoGF,matricula);
        String url=String.format("jdbc:mysql://%s:%s/%s",
                ubicacionmysl,puertomysql,basedatos);
        System.out.println("URL MySQL:"+url);

        String pagina= HttpUtils.httpGet(urlbaseGF, null);
        int posInicioTitulo=pagina.indexOf("<title>")+7;
        int posFinTitulo=pagina.indexOf("</title>");
        int posInicioH1=pagina.indexOf("<h1>")+4;
        int posFinH1=pagina.indexOf("</h1>");
        int posInicioH2=pagina.indexOf("<h2>")+4;
        int posFinH2=pagina.indexOf("</h2>");
        String textoTitulo=pagina.substring(posInicioTitulo, posFinTitulo).toUpperCase();
        String textoH1=pagina.substring(posInicioH1, posFinH1).toUpperCase();
        String textoH2=pagina.substring(posInicioH2, posFinH2).toUpperCase();
        String tituloEsperado=String.format("Servicios REST de %s",nombreCompleto).toUpperCase();
        String h2Esperado=String.format("Matricula %s",matricula).toUpperCase();

        assertEquals(tituloEsperado,textoTitulo);
        assertEquals(tituloEsperado,textoH1);
        assertEquals(h2Esperado,textoH2);

        dao=new DaoConcursosREST(ubicacionGF,Integer.parseInt(puertoGF),matricula);

        databaseTester=new JdbcDatabaseTester(driverName,url,
                usuario,clave);
        databaseTester.setOperationListener(new CustomConfigurationOperationListener());
        conndbunit=databaseTester.getConnection();
        DatabaseConfig config=conndbunit.getConfig();
        config.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS,true);
        IDataSet dataSet=new FlatXmlDataSetBuilder().build(new FileInputStream("concursosv3.xml"));
        databaseTester.setDataSet(dataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.onSetup();
    }

    @AfterAll
    public static void termina() throws Exception {
        databaseTester.setTearDownOperation(DatabaseOperation.REFRESH);
        databaseTester.onTearDown();
        if (calif_inst>0) {
            System.out.printf("Puntos para Pruebas Institucion: %.2f/%.2f\n", calif_inst * PORCENTAJE_INST / MAX_CALIF_INST, PORCENTAJE_INST);
        }
    }

    /// INSTITUCION

    private void comparaInst(Institucion actual, ITable expected, int numrow) {
        try {
            assertEquals(String.valueOf(actual.getIdInstitucion()), expected.getValue(numrow, "id_institucion").toString());
            assertEquals(actual.getNombreInstitucion(), expected.getValue(numrow, "nombre_institucion"));
            assertEquals(actual.getNombreCortoInstitucion(), expected.getValue(numrow, "nombre_corto_institucion"));
            assertEquals(actual.getUrlInstitucion(), expected.getValue(numrow, "url_institucion"));
            assertEquals(actual.getCalleNumInstitucion(), expected.getValue(numrow, "calle_num_institucion"));
            assertEquals(actual.getColoniaInstitucion(), expected.getValue(numrow, "colonia_institucion"));
            assertEquals(String.valueOf(actual.getIdMunicipioInstitucion()), expected.getValue(numrow, "id_municipio_institucion").toString());
            assertEquals(String.valueOf(actual.getIdEntidadInstitucion()), expected.getValue(numrow, "id_entidad_institucion").toString());
            assertEquals(actual.getCodpostalInstitucion(), expected.getValue(numrow, "codpostal_institucion"));
            assertEquals(actual.getTelefonoInstitucion(), expected.getValue(numrow, "telefono_institucion"));
        }
        catch (Exception ex) {
            //ex.printStackTrace();
            assertNull("No deberia generar excepcion comparar las instituciones",ex);
        }
    }

    @Test
    @Order(2)
    public void testInstObten() throws Exception {
        List<Institucion> actual = dao.obtenInstituciones();

        IDataSet expectedDataSet=new FlatXmlDataSetBuilder().build(new File("concursosv3.xml"));
        ITable expectedTable=expectedDataSet.getTable("institucion");

        assertEquals(actual.size(),expectedTable.getRowCount());
        for (int i=0; i<actual.size(); i++) {
            comparaInst(actual.get(i), expectedTable,i);
        }
        calif_inst += CALIF_OBTENER;
    }
}
