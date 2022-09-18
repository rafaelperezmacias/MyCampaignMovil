package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rld.app.mycampaign.R;

public class PolicyFragment extends Fragment {

    private CheckBox checkBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_policy_volunteer_bs, container, false);

        checkBox = view.findViewById(R.id.checkbox);

        TextView  textView = view.findViewById(R.id.textview);
        textView.setText(
                "El partido político estatal FUTURO, ubicado en la calle Mazamitla 3062, colonia Vallarta poniente, Guadalajara, Jalisco, código postal 44110; es el responsable de la protección y uso de los datos personales que se recaben.\n\n" +
                "El tratamiento de los datos personales que sean recabados se realizan con fundamento en lo establecido en los artículos 6°, apartado A, fracciones I, II y IV, y 16 párrafo segundo de la Constitución Política de los Estados Unidos Mexicanos; artículos 4° y 9°, fracciones II, V y VI de la Constitución Política del Estado de Jalisco; artículos 3, fracción II, 18, 26, 27 y 28 de la Ley General de Protección de Datos Personales en Posesión de Sujetos Obligados; artículos 3, fracciones III y XXXII, 5, 10, 19, 20, 24 y 87 fracciones III y X de la Ley de Protección de Datos Personales en Posesión de Sujetos Obligados del Estado de Jalisco y sus Municipios; artículos 1, 70 y 76 de la Ley General de Transparencia y Acceso a la Información Pública; los artículos 8, 16 y 25, fracción XVIII de la Ley de Transparencia y Acceso a la Información Pública del Estado de Jalisco y sus Municipios; artículos 30 párrafo primero inciso g), 41 párrafo primero inciso c), 43 párrafo primero incisos b) y d), 44 y 63 párrafo primero inciso e) de la Ley General de Partidos Políticos; artículo 38 fracción IV del Código Electoral del Estado de Jalisco; artículos 12, 35 párrafo primero, 40, 41, 42, 43, 44, 46 y transitorio segundo de los Estatutos del partido político local FUTURO; e inciso B) de la Convocatoria para la integración de la Comisión Ejecutiva Estatal 2020-2023.\n\n" +
                "Los datos personales a los cuales se les dará tratamiento para la integración de los expedientes de las personas postulantes para la convocatoria son los siguientes:\n\n" +
                "Datos de identificación y localización. Nombre completo, edad, fotografía, lugar de residencia, correo electrónico particular, número de afiliación, registro federal de contribuyentes, estado civil, nacionalidad, domicilio, giro comercial, teléfono, clave única de registro poblacional y datos bancarios.\n\n" +
                "Datos académicos, de experiencia y laborales. Carrera de estudio (institución y profesión), empleos anteriores (cargo, período e institución), publicaciones e investigaciones (tipo, medio y año de publicación).\n\n" +
                "Datos para declaraciones fiscales, patrimoniales y de intereses. Nombre, nacionalidad, relación, ingresos anuales, bienes inmuebles (tipo, ubicación y valor), vehículos (tipo, registro, marca, modelo y valor), bienes muebles (tipo y valor), inversiones, cuentas bancarias y valores (tipo, ubicación, nombre entidad y saldo), participación en direcciones y consejos (nombre de la institución, ubicación, actividad económica, tipo de participación y monto anual), participación accionaria (nombre de la institución, ubicación, antigüedad, porcentaje de participación), préstamos, créditos y obligaciones (nombre de acreedor, tipo, monto original y actual), posiciones y cargos (nombre de la institución, cargo, duración y monto anual), intereses diversos (nombre de la institución, cargo y vigencia), viajes financiados (nombre de financiador, fecha de viaje, ubicación y valor de viaje), patrocinios, cortesías y donativos (nombre de donante, propósito y valor).\n\n" +
                "Datos sensibles. Firma y fotografía.\n\n" +
                "Los datos personales son recabados directamente del titular de los datos salvo aquellos terceros involucrados en las declaraciones patrimoniales y de intereses; por lo que la persona que entregue datos personales de terceros deberá de notificar a sus titulares este aviso de privacidad previo al aprovechamiento de sus datos personales.\n\n" +
                "Los datos personales son recabados para las siguientes finalidades: Suscribir su intención de participar en la convocatoria abierta a la Comisión Ejecutiva Estatal de Futuro; Compartir con la militancia sus versiones públicas de las declaraciones patrimoniales, de interés y fiscal de los interesados en participar en la convocatoria; Compartir con la militancia el resto de documentación enviada para participar en la convocatoria abierta a la Comisión Ejecutiva Estatal de Futuro; Servir como fin informativo para la Asamblea General a efecto de que esta pueda decidir su voto para la conformación de la Comisión Ejecutiva Estatal; Llevar el registro público de los padrones de proveedores y gastos relacionados a honorarios, prestación de servicios, asesorías y comunicación social que son requeridos por la Ley General de Transparencia y Acceso a la Información Pública, y la Ley de Transparencia y Acceso a la Información Pública del Estado de Jalisco y sus Municipios; así como la elaboración de los contratos para dar certeza jurídica de la relación que se establecerá.\n\n" +
                "Finalidades secundarias: para fines estadísticos, de envío de información, de contacto directo, de creación, invitación, colaboración, de elaboración y desarrollo de análisis de datos y de directorios en términos colectivos o demográficos.\n\n" +
                "Si usted NO desea que sus datos personales sean tratados para las finalidades secundarias señaladas, o alguna de ellas, puede negarnos su consentimiento desde este momento enviándonos su solicitud al correo electrónico: transparencia@hayfuturo.mx. Su negativa no será motivo para dejar de contactarles y realizar las demás finalidades.\n\n" +
                "Se informa que no se realizarán transferencias de sus datos personales a sujetos obligados diferentes a FUTURO y que NO se consideran transferencias las comunicaciones internas entre órganos internos de FUTURO.\n\n");

        return view;
    }

    public boolean isComplete() {
        return !checkBox.isChecked();
    }
}
