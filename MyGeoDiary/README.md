Version 29.03.2023

# Aufgabenstellung Team MyGeoDiary

**Ausbaustufe 1: Offene Daten und Algorithmen**

- Ausschließlich englischsprachige Anwendung
- Implementierung als JAVA Konsolenanwendung.
- Agile Entwicklung
- Als ausschließlich englischsprachiges Open Source Project unter Account Prof. Weinkauf auf GIT HUB
  - [Eine Open-Source-Lizenz](https://help.github.com/articles/open-source-licensing/#where-does-the-license-live-on-my-repository)
  - [Eine README](https://help.github.com/articles/create-a-repo/#commit-your-first-change)
  - [Beitragsrichtlinien](https://help.github.com/articles/setting-guidelines-for-repository-contributors/)
  - [einen Verhaltenskodex](https://opensource.guide/de/code-of-conduct/)
- Erfassung der eigenen Bewegung durch externe APP wie OSMAnd und speichern im GPS Exchange Format (\*.gpx)
- Layer mit Geoinformationen (OpenData) zusammenstellen für Testgebiet
  - Eigene Bewegungsstrecken pro Tag
  - Autobahnen
  - Straßen
  - _Radwege_
  - Bahnstrecke
  - Kategorisierte PoI's
- Operationen:
  - Bewegungsabschnitte bilden
  - Bewegungsgeschwindigkeit pro Abschnitt ermitteln
  - Abschnitt verschneiden
  - Signifikante Verweildauer an PoI's ermitteln
  - =\> alle Zwischenergebnisse so abspeichern, das die Zwischenergebnisse möglichst im GIS visualisierbar sind. Idee: Ordnerhierarchie nutzen und Zwischenergebnisse als Geoobjektdatendateien (z.B. Shappedatei oder WKT oder GML) abspeichern. In GIS wie QGIS sollte man die Zwischenergebnisse als Layer einladen und sich ansehen können …
- Ausgabe: Automatisches Tagebuch in Textform (plain Textdatei erzeugen), z.B.
  - You waked up at 6:45 a.m.
  - At 7:45 you went by car to Merseburg.
  - You arrived in Merseburg at 8:35.
  - Probably breakfast at McDonalds.
  - Then by car to the university. You stayed there till 4:45 p.m.
  - Back home by car
  - After two hours at home we went to the gym by bike.
  - …
  - You went to bed at 11:12 p.m.

Zusätzlich: wiss. Evaluation durch Software Experiment nach Wohlin …

- Experimentalplanung nach Wohlin inkl. RQ und Hypothesen
- Testpersonen zeichnen ihre Tagesroute auf
- Verarbeitung
- Testpersonen geben an, welche Ergebnisse korrekt sind
- Wiss. Datenbasis: Tests
- Statistische Auswertung der Tests durch wiss. Statistiksoftware der HoMe
- …Reporting nach Wohlin

![](RackMultipart20230502-1-enypjf_html_ea55494d586c7f64.png)

**Ausbaustufe 2: APP MyGeoDiary (Ausblick, nicht Aufgabe in diesem Semester)**

Entwicklung einer App, die die benötigten Geoinformationen dynamisch über Geodienste (WFS) lädt, selbst das Bewegungsprofil aufzeichnet und in der App automatisch das GeoDiary führt.