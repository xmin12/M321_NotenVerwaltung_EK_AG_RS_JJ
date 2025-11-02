# Notenverwaltungssystem

## Projektziele

* Jede*r entwickelt ein vollständiges Feature (Frontend + Backend + DB)
* Features sind klar voneinander abgegrenzt → weniger Merge-Konflikte
* Am Ende entsteht ein komplettes, funktionsfähiges System
* Jede*r präsentiert den eigenen Teilbereich

---

## Team

* Jasmin Jeyakumar
* Ruben Schneebeli
* Elmar Kessler
* Aurora Gjemaj

---

## Technologien

* Frontend: React mit TypeScript
* Backend: Java Spring Boot
* Datenbank: PostgreSQL
* Deployment: Docker

---

## Features / Systemkomponenten

* Benutzerverwaltung – Ruben
* Klassenverwaltung – Elmar
* Notenerfassung – Jasmin
* Statistik & Auswertung – Aurora

(Hinweis: Jede Person übernimmt ein Feature inkl. Frontend, Backend und DB-Anbindung.)

---

## Abgabe – Erwartete Inhalte

Laut Vorgaben müssen folgende Punkte vorhanden sein:

* Dokumentation

  * Titelblatt
  * Inhaltsverzeichnis
  * Vorgehen (Projektplanung)
  * Anforderungen & Features
  * Überblick des verteilten Systems (inkl. Grafik)
  * Dokumentation der einzelnen Systemkomponenten
  * Abschlissende Worte (Fazit, Ausblick)
  * Anhang (Quellen, Testprotokolle etc.)

* Code

  * Link zum Git-Repository oder ZIP-Archiv
  * Strukturierte Abgabe (pro Komponente oder gemeinsames Repo)
  * Finaler Branch klar gekennzeichnet

* Präsentation

  * Jede*r stellt die eigene Komponente vor
  * Gesamtsystem wird lauffähig gezeigt

---

## Testprotokoll – Microservices Projekt

**Projekt:** Schulmanagement-System
**Version:** 1.0
**Datum:** [Datum einfügen]
**Tester:** [Name einfügen]

**Umfang:** Dieses Protokoll dokumentiert die Unit-Tests der Microservices **user-service**, **grade-service** und **class-service**. Ziel ist es, die Abdeckung und Funktionalität der Entitäten, Repositories und Controller zu überprüfen. Besonderes Augenmerk liegt auf Fehlerbehandlung und erwarteten HTTP-Statuscodes.

---

## 1. User-Service Testprotokoll

**Beschreibung:**
Der **user-service** verwaltet Benutzerdaten. Die Tests decken die **User Entität**, das **UserRepository** und den **UserController** ab.

### 1.1 UserTest.java

**Zweck:** Überprüfung der grundlegenden Funktionalität der User-Entität
**Abdeckung:**

* Standardkonstruktor: Instanziierung ohne Argumente
* Parametrisierter Konstruktor: Korrekte Initialisierung aller Felder (id, name, email, password, role)
* Getter/Setter: Korrekte Rückgabe und Aktualisierung von Feldern
* Role Enum: Korrekte Definition und Namenskonvention
  **Ergebnis:** Alle Methoden der User Entität funktionieren erwartungsgemäß

### 1.2 UserRepositoryTest.java

**Zweck:** Überprüfung der Datenzugriffsschicht mit H2 In-Memory-Datenbank

| Testfall           | Beschreibung                                           | Ergebnis  |
| ------------------ | ------------------------------------------------------ | --------- |
| testSaveUser()     | Speichert neuen Benutzer, prüft ID und Daten           | Bestanden |
| testFindById()     | Abrufen existierender und nicht existierender Benutzer | Bestanden |
| testFindAllUsers() | Alle Benutzer abrufen                                  | Bestanden |
| testFindByRole()   | Benutzer nach Rolle filtern                            | Bestanden |
| testUpdateUser()   | Benutzer aktualisieren                                 | Bestanden |
| testDeleteUser()   | Benutzer löschen                                       | Bestanden |

**Ergebnis:** CRUD-Operationen und benutzerdefinierte Abfragen funktionieren zuverlässig

### 1.3 UserControllerTest.java

**Zweck:** Überprüfung der REST-API-Endpunkte mit MockMvc
**Abdeckung:**

* GET /api/users → 200 OK
* GET /api/users/{id} → 200 OK / 404 Not Found
* GET /api/users/role/{role} → 200 OK / 400 Bad Request
* POST /api/users → 200 OK
* PUT /api/users/{id} → 200 OK / 404 Not Found
* DELETE /api/users/{id} → 204 No Content / 404 Not Found

**Ergebnis:** Endpunkte reagieren korrekt auf Anfragen, Fehler werden robust behandelt

---

## 2. Grade-Service Testprotokoll

**Beschreibung:**
Der **grade-service** verwaltet Notendaten. Die Tests decken **Grade Entität**, **GradeRepository** und **GradeController** ab. Zusätzlich wurden alle Endpunkte mit **Postman** getestet und alle Anfragen waren erfolgreich.

### 2.1 GradeTest.java

* Standard- und parametrisierter Konstruktor
* Getter/Setter, equals(), hashCode(), toString()
  **Ergebnis:** Methoden funktionieren erwartungsgemäß

### 2.2 GradeRepositoryTest.java

| Testfall              | Beschreibung                             | Ergebnis  |
| --------------------- | ---------------------------------------- | --------- |
| testSaveGrade()       | Note speichern                           | Bestanden |
| testFindById()        | Existierende und nicht existierende Note | Bestanden |
| testFindAllGrades()   | Alle Noten abrufen                       | Bestanden |
| testFindByStudentId() | Noten für Schüler abrufen                | Bestanden |
| testFindByClassId()   | Noten für Klasse abrufen                 | Bestanden |
| testFindBySubject()   | Noten für Fach abrufen                   | Bestanden |
| testUpdateGrade()     | Note aktualisieren                       | Bestanden |
| testDeleteGrade()     | Note löschen                             | Bestanden |

### 2.3 GradeControllerTest.java

* GET /api/grades → 200 OK
* GET /api/grades/{id} → 200 OK / 404 Not Found
* GET /api/grades/student/{studentId} → 200 OK
* GET /api/grades/class/{classId} → 200 OK
* POST /api/grades → 201 Created
* PUT /api/grades/{id} → 200 OK / 404 Not Found
* DELETE /api/grades/{id} → 204 No Content / 404 Not Found

**Ergebnis:** Endpunkte funktionieren korrekt, Fehlerfälle werden robust gehandhabt

### 2.4 Postman-Tests

Zusätzlich zu den Unit-Tests wurden alle Endpunkte des **Grade-Service** mit **Postman** getestet. Alle Anfragen waren erfolgreich, wie im folgenden Bild zu sehen ist:

![Screenshot 2025-11-02 160228.png](https://d41chssnpqdne.cloudfront.net/user_upload_by_module/chat_bot/files/95053572/Oh1l0nTLMeVNrpE3.png?Expires=1763336402&Signature=b374hwQwWelW7yoaSxDlj8f0VnPPahz~YmlqP52R-wgNcV4maw7iO4tb5bXkXj5ewtEIBds6kLHsRescQ4Z5lPLAt-QKT1z5DYqeWg5HIgi5XQfdxd~Hc~fYH8E~ArHNaSXr9VCJ15218cRkKqYAxYflOUPnxwowLBWTZLTNqoGaHz-0nuozpDbi3KB55BCHtkDAQJCNRTX71h-zkDDE3SBhpoO3Qk9MsJHN2PMMCFilLy6hIFhU3coM9J0jz6pfMwDUWZrguoUZMuMyZtoQqn2gF28PS30q9k4IBMiGbLWqTnKKAeIqsffgmAZM1Fh9YjNXxFzsBpjyy8H5x8FMRg__&Key-Pair-Id=K3USGZIKWMDCSX)

---

## 3. Class-Service Testprotokoll

**Beschreibung:**
Der **class-service** verwaltet Klassendaten. Die Tests decken **ClassEntity**, **ClassService** und **ClassController** ab.

### 3.1 ClassEntityTest.java

* Getter/Setter für id, name, teacherId, studentIds
  **Ergebnis:** Funktioniert korrekt

### 3.2 ClassServiceTest.java

| Testfall                          | Beschreibung           | Ergebnis  |
| --------------------------------- | ---------------------- | --------- |
| testGetAllClasses()               | Alle Klassen abrufen   | Bestanden |
| testGetClassByIdFound / NotFound  | Klasse nach ID abrufen | Bestanden |
| testCreateClass()                 | Klasse erstellen       | Bestanden |
| testUpdateClassSuccess / NotFound | Klasse aktualisieren   | Bestanden |
| testDeleteClass()                 | Klasse löschen         | Bestanden |
| testAddStudentToClass             | Schüler hinzufügen     | Bestanden |
| testRemoveStudentFromClass        | Schüler entfernen      | Bestanden |
| testChangeTeacher                 | Lehrer ändern          | Bestanden |

### 3.3 ClassControllerTest.java

* GET /api/classes → 200 OK
* GET /api/classes/{id} → 200 OK / 404 Not Found
* POST /api/classes → 200 OK
* PUT /api/classes/{id} → 200 OK / 404 Not Found
* DELETE /api/classes/{id} → 200 OK
* POST /api/classes/{classId}/students/{studentId} → 200 OK / 404 Not Found
* DELETE /api/classes/{classId}/students/{studentId} → 200 OK / 404 Not Found
* PUT /api/classes/{classId}/teacher/{teacherId} → 200 OK / 404 Not Found

**Ergebnis:** Endpunkte reagieren korrekt, Fehlerbehandlung über RuntimeException → HTTP 404

---

## 4. Gesamtfazit

Die Unit-Tests der Microservices **user-service**, **grade-service** und **class-service**:

* Bieten **umfassende Abdeckung** der Entitäten, Repositories und Controller
* Prüfen **CRUD-Operationen**, Filterung, Geschäftslogik und Fehlerbehandlung
* Stellen sicher, dass **API-Endpunkte die korrekten Daten und HTTP-Statuscodes** zurückgeben
* Gewährleisten die **Robustheit und Zuverlässigkeit** der Microservices

Zusätzlich wurden alle Endpunkte des **Grade-Service** mit **Postman** getestet, und alle Anfragen waren erfolgreich, wie im oben gezeigten Bild zu sehen ist.
