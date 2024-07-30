package com.example.registerotp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentSplaschBinding;
import com.example.registerotp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplaschFragment extends Fragment {

    private FragmentSplaschBinding binding;
    private NavController navController;
    private FirestoreHelper firestoreHelper;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSplaschBinding.inflate(inflater, container, false);


        /*firestoreHelper = new FirestoreHelper();

        Map<String, List<String>> professionsMap = new HashMap<>();
        professionsMap.put("Architekt", Arrays.asList("Gebäude entwerfen", "Baupläne erstellen", "Bauprojekte überwachen", "Skizzen anfertigen", "3D-Modelle erstellen", "Baumaterialien auswählen", "Kunden beraten", "Baugesetze einhalten", "Energieeffizienz planen", "Raumkonzepte entwickeln"));
        professionsMap.put("Bauingenieur", Arrays.asList("Bauprojekte planen", "Baustatik berechnen", "Bauüberwachung", "Baustellenbesuche", "Tragwerksplanung", "Bauabläufe koordinieren", "Baukosten kalkulieren", "Bauqualität sichern", "Umweltverträglichkeit prüfen", "Ingenieurleistungen erbringen"));
        professionsMap.put("Bauleiter", Arrays.asList("Bauarbeiten koordinieren", "Bauqualität kontrollieren", "Bauzeitplan einhalten", "Baustellenorganisation", "Arbeiter anleiten", "Materialbeschaffung", "Bauabnahmen durchführen", "Sicherheitsvorschriften überwachen", "Baubesprechungen leiten", "Problemlösung vor Ort"));
        professionsMap.put("Maurer", Arrays.asList("Mauerwerke bauen", "Steine setzen", "Betonarbeiten", "Fundamente gießen", "Wände verputzen", "Schalungen bauen", "Klinkerarbeiten", "Baustellen einrichten", "Baustoffe mischen", "Bodenplatten herstellen"));
        professionsMap.put("Zimmerer", Arrays.asList("Holzkonstruktionen bauen", "Dachstühle errichten", "Holzrahmenbau", "Holz zuschneiden", "Holzverbindungen herstellen", "Treppenbau", "Holzschutzmaßnahmen", "Innenausbau", "Fassadenverkleidungen", "Holzdecken montieren"));
        professionsMap.put("Betonbauer", Arrays.asList("Beton mischen", "Betonbauteile herstellen", "Betonschalungen erstellen", "Stahlbetonarbeiten", "Beton gießen", "Beton verdichten", "Fertigteilmontage", "Betonoberflächen bearbeiten", "Bewehrungen einbauen", "Betonsanierung"));
        professionsMap.put("Bauzeichner", Arrays.asList("Baupläne zeichnen", "Baudetails entwerfen", "CAD-Software nutzen", "Grundrisse erstellen", "Schnittzeichnungen anfertigen", "Bauansichten gestalten", "Baubeschreibungen verfassen", "Bestandspläne aktualisieren", "Genehmigungspläne vorbereiten", "Baudokumentation"));
        professionsMap.put("Straßenbauer", Arrays.asList("Straßen bauen", "Asphaltieren", "Pflasterarbeiten", "Unterbau herstellen", "Entwässerungssysteme einbauen", "Bordsteine setzen", "Straßenmarkierungen", "Verkehrsschilder aufstellen", "Wege anlegen", "Straßensanierung"));
        professionsMap.put("Tiefbaufacharbeiter", Arrays.asList("Kanalbau", "Rohrleitungsbau", "Erdarbeiten", "Gräben ausheben", "Verbauarbeiten", "Untertagebau", "Bodenverdichtung", "Gründungsarbeiten", "Baugrunduntersuchung", "Erschließungsarbeiten"));
        professionsMap.put("Elektriker", Arrays.asList("Elektroinstallationen", "Kabel verlegen", "Sicherungen montieren", "Schaltkästen installieren", "Elektroprüfungen", "Beleuchtungssysteme einbauen", "Fehlersuche und Reparaturen", "Smart-Home-Installationen", "Wartungsarbeiten an elektrischen Anlagen", "Elektrogeräte anschließen", "Steckdosen montieren", "Sicherheitsbeleuchtung installieren", "Erdungsanlagen", "Blitzschutzanlagen", "Notstromversorgungssysteme", "Brandmeldeanlagen", "Gebäudeautomation", "Photovoltaikanlagen installieren", "Industrieanlagen elektrifizieren", "Niederspannungsanlagen", "Hochspannungsanlagen", "Kabeltrassen verlegen", "Leitungsführungssysteme", "Gebäudevernetzung", "Energieoptimierung", "Elektromotoren warten", "Schaltpläne lesen", "Netzwerkverkabelung", "Gebäudeleittechnik", "Elektroheizungen installieren", "Elektroinstallationen planen", "Gefahrenstellen absichern", "Elektroisolierungen", "Leitungsnetze aufbauen", "Fernmeldetechnik", "Elektrische Messungen", "Transformatoranlagen", "Datenübertragungssysteme", "Leistungsschalter einrichten", "Solarwechselrichter", "Blitzableiter installieren", "Elektrische Prüfungen", "Energieversorgungssysteme", "Kabelverbindungen prüfen", "Datenleitungen verlegen", "Glasfasertechnik", "Kabeltrassen bauen", "Leistungskontrolle", "Elektrische Geräte warten"));
        professionsMap.put("Sanitär-, Heizungstechniker", Arrays.asList("Sanitäranlagen installieren", "Heizungen einbauen", "Klimaanlagen warten", "Rohrleitungen verlegen", "Bäder sanieren", "Wartungsarbeiten", "Solaranlagen installieren", "Lüftungssysteme einbauen", "Wasserenthärtungsanlagen", "Energieberatung"));
        professionsMap.put("Klimatechniker", Arrays.asList("Klimaanlagen installieren", "Klimaanlagen warten", "Klimaanlagen reparieren", "Kältemittel nachfüllen", "Klimaanlagen reinigen", "Lüftungssysteme einbauen", "Raumlufttechnik", "Klimasysteme testen", "Thermostate einstellen", "Klimaanlagen entlüften", "Ventilatoren installieren", "Kühlanlagen montieren", "Wärmerückgewinnungssysteme", "Klimaanlagen optimieren", "Kälteleitungen verlegen", "Dichtheitsprüfungen", "Klimasysteme modernisieren", "Wartungsprotokolle führen", "Energiemanagement", "Filter wechseln", "Klimasteuerungen programmieren", "Klimaanlagen dimensionieren", "Schalldämpfer installieren", "Klimatisierung planen", "Luftfeuchtigkeitsregelung", "Klimaanlagen einregulieren", "Kältemaschinen bedienen", "Luftverteilungssysteme", "Heizungs- und Kühlkombisysteme", "Raumluftqualität überwachen", "Luftkanäle reinigen", "Klimaanlagen entkalken", "Luftkühler einbauen", "Isolierungen anbringen", "Brandschutzklappen installieren", "Klimaautomationssysteme", "Klimaanlagen diagnostizieren", "Lecksuche durchführen", "Luftfilter überprüfen", "Klimaanlagen-Desinfektion", "Klimaanlagen entfeuchten", "Klimageräte konfigurieren", "Kältemittelrecycling", "Rohrleitungsbau für Kälteanlagen", "Klimaanlagen nachrüsten"));
        professionsMap.put("Maler und Lackierer", Arrays.asList("Wände streichen", "Lackieren", "Oberflächen gestalten", "Tapezieren", "Fassadenanstrich", "Graffitientfernung", "Dekorative Techniken", "Holzschutzanstriche", "Schimmelbekämpfung", "Trockenbauarbeiten", "Spachteln", "Grundieren", "Lasieren", "Farbberatung", "Wandmalerei", "Beschichtungen auftragen", "Lackschäden ausbessern", "Verputzarbeiten", "Brandschutzbeschichtungen", "Dekorputze", "Beschriftungen", "Schablonentechniken", "Strukturputz", "Malerarbeiten im Innenbereich", "Malerarbeiten im Außenbereich", "Fensterrahmen lackieren", "Türen streichen", "Möbel lackieren", "Metall lackieren", "Korrosionsschutz", "Fußleisten anbringen", "Trockenbauwände spachteln", "Anstrich vorbereiten", "Farbmischung", "Arbeitsplatz einrichten", "Farbspritztechnik", "Fassadenreinigung", "Verputzen", "Abdeckarbeiten", "Schleifarbeiten", "Dämmputze", "Betonsanierung", "Graffitischutz", "Sanierungsarbeiten", "Fugenarbeiten", "Innenanstriche", "Außenanstriche", "Dekorationsmalerei", "Rauputz", "Sanierputz"));
        professionsMap.put("Klempner", Arrays.asList("Sanitäranlagen installieren", "Rohrleitungen verlegen", "Heizungsanlagen einbauen", "Wartungsarbeiten", "Reparaturen durchführen", "Lecksuche", "Dichtungen erneuern", "Abwasserleitungen reinigen", "Wasserhähne austauschen", "Toiletten installieren", "Duschen einbauen", "Waschbecken montieren", "Wasserleitungen isolieren", "Armaturen montieren", "Heizkörper anschließen", "Sanitärkeramik einbauen", "Abflussrohre freimachen", "Entlüftungssysteme installieren", "Solaranlagen anschließen", "Wasserversorgungssysteme planen", "Gasleitungen installieren", "Boiler reparieren", "Druckprüfungen durchführen", "Pumpensysteme warten", "Notdienst", "Sanitärinstallationen modernisieren", "Trinkwasserleitungen prüfen", "Thermostate installieren", "Klempnerarbeiten im Neubau", "Klempnerarbeiten im Altbau", "Wasserenthärtungsanlagen einbauen", "Kundendienst", "Energieberatung", "Rohrleitungen löten", "Installationspläne lesen", "Bauteile abdichten", "Fußbodenheizung verlegen", "Brennwertkessel installieren", "Gasthermen warten", "Kalkschutzsysteme installieren", "Abwassertechnik", "Regenwassernutzungssysteme installieren", "Gebäudeentwässerung", "Sicherheitsprüfungen", "Kompressoren installieren", "Wasseraufbereitungssysteme", "Regenrinnen installieren", "Sanitärtechnik", "Warmwasserspeicher montieren"));
        professionsMap.put("Fliesenleger", Arrays.asList("Fliesen verlegen", "Untergrund vorbereiten", "Fliesenkleber auftragen", "Fliesen schneiden", "Fugen ausfüllen", "Wandfliesen setzen", "Bodenfliesen verlegen", "Feinsteinzeug verarbeiten", "Natursteinfliesen", "Silikonfugen", "Fliesenreparaturen", "Dämm- und Sperrschichten", "Badsanierung", "Fliesenspiegel anbringen", "Abdichtungen", "Estricharbeiten", "Sockelleisten montieren", "Mosaikfliesen setzen", "Bodenbeläge aus Fliesen", "Terrassenfliesen", "Treppenfliesen", "Heizestrich verlegen", "Dekorfliesen", "Fliesenmuster erstellen", "Zementfliesen", "Fliesenfugen reinigen", "Schallschutzmaßnahmen", "Feuchtigkeitsschutz", "Fliesenentfernung", "Arbeiten nach Plan", "Schutzausrüstung verwenden"));
        professionsMap.put("Plattenleger", Arrays.asList("Platten verlegen", "Untergrund vorbereiten", "Platten schneiden", "Plattenfugen ausfüllen", "Großformatige Platten", "Natursteinplatten", "Terrassenplatten", "Bodenplatten", "Wandverkleidung mit Platten", "Betonwerksteinplatten", "Plattenkleber auftragen", "Plattenreparaturen", "Dämm- und Sperrschichten", "Außenbereichsplatten", "Treppenplatten", "Dekorplatten", "Estricharbeiten", "Sockelleisten montieren", "Plattenmuster erstellen", "Plattenfugen reinigen", "Schallschutzmaßnahmen", "Feuchtigkeitsschutz", "Plattenentfernung", "Arbeiten nach Plan", "Schutzausrüstung verwenden"));
        professionsMap.put("Mosaikleger", Arrays.asList("Mosaike erstellen", "Mosaiksteine setzen", "Untergrund vorbereiten", "Mosaikmuster entwerfen", "Mosaikflächen kleben", "Fugen ausfüllen", "Glasmosaik verarbeiten", "Keramikmosaik", "Natursteinmosaik", "Wandmosaik", "Bodenmosaik", "Dekorative Mosaike", "Restaurierung von Mosaiken", "Künstlerische Gestaltung", "Mosaikarbeiten im Innenbereich", "Mosaikarbeiten im Außenbereich", "Mosaikreparaturen", "Mosaikbilder", "Schablonentechniken", "Mosaikverkleidung", "Mosaikfugen reinigen", "Feinsteinzeugmosaik", "Mosaikplatten schneiden", "Schallschutzmaßnahmen", "Feuchtigkeitsschutz", "Mosaikentfernung", "Arbeiten nach Plan", "Schutzausrüstung verwenden"));
        professionsMap.put("Schreiner Tischler", Arrays.asList("Möbel herstellen", "Holzarbeiten", "Reparaturen durchführen", "Fensterbau", "Türen einbauen", "Innenausbau", "Holzverbindungen herstellen", "Oberflächenbehandlung", "Maßanfertigungen", "Möbel restaurieren", "Treppenbau", "Holzböden verlegen", "Dachausbau", "Einbauschränke", "Holzfenster restaurieren", "Fensterläden bauen", "Holzdecken montieren", "Parkett verlegen", "Küchenmöbel bauen", "Holzdielen schleifen", "Holztreppen renovieren", "Fensterrahmen reparieren", "Holzgeländer montieren", "Holzkonservierung", "Werkzeugpflege", "Holzbearbeitungsmaschinen", "Holzkonstruktionen", "Furniere schneiden", "Holztrocknung", "Baustellenorganisation", "Holzschutzmittel", "Holzbauwerke", "Schreinerpläne lesen", "Holztechnik", "Möbelrestaurierung", "Holzfassadenbau", "Holzkonstruktionen berechnen", "Bauwerksabdichtung", "Baubeschläge montieren", "Fassadenverkleidung", "Massivholzmöbel"));
        professionsMap.put("Dachdecker", Arrays.asList("Dächer eindecken", "Dachreparaturen durchführen", "Dachabdichtungen", "Dachfenster einbauen", "Schneefanggitter montieren", "Dachrinnen installieren", "Flachdacharbeiten", "Schieferarbeiten", "Gründachbau", "Dachstühle sanieren", "Dachziegel montieren", "Dachgauben einbauen", "Schindeln verlegen", "Dachisolierung", "Asphaltdach abdichten", "Dachflächenfenster einsetzen", "Flüssigabdichtungen", "Blechdächer montieren", "Schornsteine verkleiden", "Dachwartung", "Bauklempnerei", "Holzschindeln anbringen", "Dachbegrünung", "Kupferdächer", "Tonziegel verlegen", "Dachpappe verlegen", "Dachnotrufsysteme", "Dachziegel reinigen", "Regenrinnen reparieren", "Dachbeläge ausbessern", "Dachdurchführungen abdichten", "Dachentwässerung", "Dachdämmung", "Dachbeschichtungen", "Dachanschlüsse herstellen", "Blechverarbeitung", "Dachstuhlverstärkung", "Dachschindeln austauschen", "Bauaufzüge verwenden"));
        professionsMap.put("Metallbauer", Arrays.asList("Metallkonstruktionen fertigen", "Schweißen", "Montagearbeiten durchführen", "Metallbearbeitung", "Geländerbau", "Tore und Türen anfertigen", "Blechbearbeitung", "Metallveredelung", "Schlosserei", "Bauteilmontage", "Metallkonstruktionen planen", "Edelstahlverarbeitung", "Gussarbeiten", "Metallprofile schneiden", "Aluminiumverarbeitung", "Stahlbau", "Maschinenbedienung", "Metallgestaltung", "Metallrohre biegen", "Metallgehäuse fertigen", "Fassadenbau aus Metall", "Metallgitter herstellen", "Schmiedearbeiten", "Metallrahmen montieren", "Stahlrohre schweißen", "Metallplatten schneiden", "Blechzuschnitte anfertigen", "Baugruppen montieren", "Metallkonservierung", "Geländersysteme montieren", "Metallskulpturen", "Maschineninstandhaltung", "Metallgeräte bauen", "Metalltechnik", "Metallgeländer reparieren", "Schmiedeeisen", "Metallfassaden", "Baugruppen montieren", "Schweißroboter bedienen", "CNC-Maschinen programmieren"));
        professionsMap.put("Glaser", Arrays.asList("Glas zuschneiden", "Fenster einbauen", "Glasfassaden montieren", "Glasreparaturen durchführen", "Spiegel herstellen", "Glasveredelung", "Duschkabinen einbauen", "Glasbau", "Isolierglas", "Bleiverglasung", "Glasplatten montieren", "Sicherheitsglas", "Glasbearbeitungsmaschinen bedienen", "Glasduschen reparieren", "Verglasungen", "Glasvordächer montieren", "Glasbruchversicherung", "Glasnotdienst", "Glasverklebung", "Glasregale montieren", "Glaskuppeln bauen", "Glasoberflächen schleifen", "Sicherheitsverglasung", "Schaufenster montieren", "Glasgestaltung", "Glasbeschichtungen", "Glasgravuren", "Glaslackierung", "Glasbiegen", "Bauverglasung", "Glaszuschnittsmaßanfertigung", "Glasbehandlung", "Glasmosaik", "Glasüberdachungen", "Antireflexglas", "Drahtglas", "Glasschutzfolien", "Glasreinigung"));
        professionsMap.put("Stuckateur", Arrays.asList("Stuckarbeiten", "Wände verputzen", "Dekorative Oberflächen gestalten", "Fassadenrestaurierung", "Trockenbauarbeiten", "Wärmedämmung", "Innenausbau", "Stuckprofile herstellen", "Baustellen vorbereiten", "Gipsarbeiten", "Außenputz", "Lehmputz", "Stuckdecken", "Stuckelemente anfertigen", "Verputztechniken", "Fassadengestaltung", "Stuckdekoration", "Putzarbeiten", "Stuckrestaurierung", "Stuckmuster anfertigen", "Kalkputz", "Stuckgips", "Gipskartonplatten montieren", "Fassadenputz", "Deckenverkleidungen", "Stuckornamente", "Säulenverkleidungen", "Trockenestriche", "Malerarbeiten", "Stuckbearbeitungsmaschinen bedienen", "Stuckrahmen", "Stuckkonservierung", "Stuckguß", "Stuckrepertoire", "Kalkfeinputz", "Gipsstuck", "Stuckstärke", "Lehmsysteme", "Mörteloberflächen", "Fassadenreinigung", "Gipskartonwände", "Trockenbauwände"));
        professionsMap.put("Schlosser", Arrays.asList("Schlösser herstellen", "Schließanlagen reparieren", "Metallarbeiten durchführen", "Türschlösser montieren", "Schlüssel anfertigen", "Sicherheitsberatung", "Schließsysteme installieren", "Wartungsarbeiten", "Tresorservice", "Edelstahlverarbeitung", "Schmiedearbeiten", "Metallbau", "Geländerbau", "Metallkonstruktionen", "Treppengeländer montieren", "Metalltüren einbauen", "Maschinenreparaturen", "Schweißarbeiten", "Metallgestaltung", "Blechbearbeitung", "Metallbauarbeiten", "Stahlbau", "Aluminiumverarbeitung", "Schlosskonstruktion", "Schlösser prüfen", "Bauschlosserei", "Metallgießerei", "Schlosserarbeiten", "Industrieschlosserei", "Metallbaukonstruktionen", "Schlosserlehre", "Feinblechbearbeitung", "Schmiedetechniken", "Schlossertätigkeit", "Metallstatik", "Metallbauerhandwerk", "Stahlprofilbearbeitung", "Metalltore herstellen", "Gusseisern", "Schmiedeverfahren"));
        professionsMap.put("Baugeräteführer", Arrays.asList("Baumaschinen bedienen", "Geräte warten", "Erdbewegungsarbeiten", "Maschinen reparieren", "Baggerarbeiten", "Kranführertätigkeiten", "Baustellen vorbereiten", "Transportarbeiten", "Maschinenpflege", "Sicherheitsprüfungen"));
        professionsMap.put("Gerüstbauer", Arrays.asList("Gerüste aufbauen", "Sicherheitsnetze installieren", "Arbeitsschutzmaßnahmen", "Fassadengerüste", "Industriegerüste", "Sonderkonstruktionen", "Baustellenabsicherung", "Gerüstverleih", "Gerüstplanung", "Montage von Arbeitsplattformen", "Lastenaufzüge", "Rahmengerüste", "Traggerüste", "Hängegerüste", "Schutzdächer", "Spezialgerüste", "Fluchttreppen", "Gerüstprüfung", "Gerüstabbau", "Gerüsttransport", "Baustellenlogistik", "Gerüstwartung", "Gerüstbauvorschriften", "Gerüstbauarbeiten", "Arbeitssicherheitsmaßnahmen", "Bauaufzüge", "Gerüstbaukonstruktionen", "Gerüstschutzplanen", "Sicherheitsgeländer", "Gerüstverankerung", "Gerüstherstellung", "Gerüstschutznetze", "Gerüsttürme", "Gerüstausstattung", "Gerüstverbindungstechnik", "Gerüstsicherungssysteme"));
        professionsMap.put("Brunnenbauer", Arrays.asList("Brunnen bohren", "Brunnenbau", "Wassergewinnung", "Pumpentechnik", "Brunnenreparaturen", "Filtertechnik", "Tiefbrunnenbohrungen", "Brunnenanlagen", "Brunnenwartung", "Brunnenpumpen", "Grundwasserabsenkung", "Wasseraufbereitung", "Brunnenanalyse", "Trinkwassergewinnung", "Sickerbrunnen", "Bewässerungsbrunnen", "Bohrlochsanierung", "Brunnenschachtbau", "Brunnenversorgung", "Wasserbehälterbau", "Brunnenkonstruktion", "Brunnenregenerierung", "Brunnenüberwachung", "Brunnenbohrmaschinen", "Brunnenfilter", "Brunnenwasseranalyse", "Pumpensteuerung", "Brunnenrohre", "Brunnenbauarbeiten", "Brunnenbautechnik", "Tiefbohrtechnik", "Brunnentechnik", "Brunnenschutz", "Brunnenabdichtung", "Brunnenpumpentechnik", "Brunnenwartungsarbeiten", "Brunnenbauunternehmen"));
        professionsMap.put("Bauwerksabdichter", Arrays.asList("Abdichtungsarbeiten", "Bauwerksabdichtung", "Feuchtigkeitsschutz", "Kellerabdichtung", "Balkonabdichtung", "Terrassenabdichtung", "Schutz vor Wasser", "Flächenabdichtung", "Bitumenabdichtung", "Kunststoffabdichtung", "Abdichtungstechnik", "Injektionstechnik", "Abdichtungssysteme", "Feuchtigkeitssperre", "Bauwerksabdichtungsarbeiten", "Sanierung von Abdichtungen", "Abdichtungsmaßnahmen", "Abdichtung von Bauwerken", "Abdichtungstechnik", "Flächenabdichtungsarbeiten", "Bauwerksabdichterarbeiten", "Abdichtungsunternehmen", "Abdichtungsplanung", "Wassersperre", "Bauabdichtungen", "Abdichtungsschicht", "Abdichtungsverfahren", "Kellerwandabdichtung", "Bauwerksabdichtungstechnik", "Flüssigabdichtung", "Bauabdichtungssysteme", "Flachdachabdichtung", "Abdichtungsbau", "Abdichtung von Balkonen"));
        professionsMap.put("Rohrleitungsbauer", Arrays.asList("Rohrleitungen verlegen", "Rohrleitungsbau", "Rohrleitungssysteme installieren", "Kanalbau", "Leitungsbau", "Abwasserleitungen verlegen", "Wasserleitungen installieren", "Gasleitungen verlegen", "Drainage verlegen", "Rohrnetze", "Rohrleitungsbauarbeiten", "Rohrleitungsinstallation", "Trinkwasserleitungen", "Entwässerungssysteme", "Heizungsrohre verlegen", "Rohrleitungsnetze", "Rohrleitungstechnik", "Kanalnetze", "Rohrverlegung", "Rohrleitungsmontage", "Rohrleitungssanierung", "Abwasserrohre", "Rohrleitungsplanung", "Rohrleitungskonstruktion", "Rohrleitungssystem", "Rohrleitungsbauunternehmen", "Rohrleitungssysteme", "Rohrleitungsprojekte", "Rohrleitungsbauarbeiten", "Rohrleitungsdurchführungen", "Rohrleitungsdienstleistungen", "Rohrleitungsbauindustrie", "Rohrleitungsbauwerk", "Rohrleitungsverbindungen", "Rohrleitungsmaterialien"));

        firestoreHelper.addMultipleProfessions("professions", professionsMap);*/


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getContext(), com.example.companies.CompanyActivity.class);
        //Intent intent = new Intent(getContext(), com.example.users.UserActivity.class);
        startActivity(intent);
        /*navController = Navigation.findNavController(view);
        ImageView sichtLogoText = binding.splaschTextLogo;
        sichtLogoText.setAlpha(0f);

        sichtLogoText.setVisibility(View.VISIBLE);
        sichtLogoText.animate().alpha(1f).setDuration(2500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Handler
                new Handler().postDelayed(() -> {
                    mAuth = FirebaseAuth.getInstance();
                    db = FirebaseFirestore.getInstance();
                    //FirebaseUtil.logout();
                    if (FirebaseUtil.isLoggedIn()) {

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        String uid = currentUser.getUid();

                        DocumentReference userRef = db.collection("id").document(uid);
                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Boolean isPrivatPerson = document.getBoolean("isPrivatPerson");
                                        if (isPrivatPerson) {
                                            //navController.navigate(R.id.id_action_to_home_page_from_splasch);
                                        }
                                        else {
                                            Intent intent = new Intent(getContext(), com.example.companies.CompanyActivity.class);
                                            //Intent intent = new Intent(getContext(), com.example.users.UserActivity.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        // Document does not exist
                                        System.out.println("No such document!");
                                    }
                                } else {
                                    System.err.println("Error getting documents: " + task.getException());
                                }
                            }
                        });

                    } else {
                        navController.navigate(R.id.id_action_registration_or_login_from_splasch);
                    }
                }, 10);
            }
        });*/



    }
}