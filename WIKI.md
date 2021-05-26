# Generator płytek drukowanych PCB

## 1. Opis

### 1.1. Problem
Jednowarstwowa płytka drukowana jest ograniczoną płaszczyzną (w naszym przypadku prostokątną), na którą nałożone zostały punkty lutownicze o podanych współrzędnych całkowitych (maksymalnie jeden punkt na parę współrzędnych). Punkty są uporządkowane parami, które należy połączyć ścieżką. 

Problem polega na takim zaprojektowaniu ścieżek, żeby dowolne dwa punkty lutownicze zostały połączone wtedy i tylko wtedy gdy było między nimi planowane połączenie - wszystkie zaplanowane pary muszą zostać połączone i żadne ścieżki nie mogę się przecinać, ani wykraczać poza obszar płytki.

Optymalne rozwiązanie to takie, które spełnia wszystkie ograniczenia, a długość ścieżek jest możliwie najkrótsza.

### 1.2. Algorytm genetyczny
Sposobem na rozwiązanie problemu projektowania płytek drukowanych są algorytmy genetyczne. Jest to przykład metaheurystyki stosowanej do rozwiązywania problemów optymalizacyjnych. Algorytm swoją nazwę zawdzięcza procesowi ewolucji organizmów, na którym jest wzorowany.

W algorytmie genetycznym tworzony jest n-elementowy zbiór losowych rozwiązań (osobników). Z tak utworzonej populacji, dokonywana jest selekcja dwóch elementów w oparciu o funkcję oceny osobników. Wybrane rozwiązania są ze sobą następnie krzyżowane, a wynikowe osobniki z pewnym prawdopodobieństwem poddawane są drobnej modyfikacji(mutacji). W wyniku selekcji, krzyżowania i mutacji otrzymujemy następne pokolenie: nową n-elementową populację, a proces rozpoczyna się od nowa. Działanie algorytmu kończy się w momencie spełnienia warunku kończącego (np. osiągnięcie określonej liczby pokoleń), a jego wynikiem jest osobnik o najlepszej wartości funkcji oceniającej.

### 1.3. Kodowanie osobnika
Każdy osobnik (rozwiązanie) posiada listę ścieżek (jedna ścieżka dla każdej pary punktów, które należy połączyć). Każda ze ścieżek posiada informacje o punkcie początkowym i końcowym, oraz uporządkowaną listę segmentów. Każdy segment ma określony kierunek (góra, dół, lewo, prawo) oraz liczbę kroków (przesunięcie w danym kierunku).

### 1.4. Inicjalizacja
Inicjalizacja polega na utworzeniu n początkowych osobników. Każdy osobnik tworzy losowe ścieżki dla każdej pary punktów do połączenia. Losowanie ścieżki polega na dodawaniu na przemian segmentów poziomych i pionowych o losowej długości i kierunku (przy czym długość jest tak ograniczona, że ścieżka nie może wykroczyć poza obszar płytki oraz prawdopodobieństwo wyboru kierunku w stronę punktu końcowego jest większe niż przeciwnego). Po wygenerowaniu ścieżki jest ona optymalizowana poprzez usunięcie zapętleń.

### 1.5. Funkcja oceny
Każdy osobnik oceniany jest wg wzoru:

P = k1w1 + k2w2 + k3w3 + k4w4 + k5w5

,gdzie:
- k1 – sumaryczna liczba segmentów wchodzących w skład ścieżek
- k2 – sumaryczna długość ścieżek
- k3 – liczba przecięć ścieżek
- k4 – sumaryczna długość fragmentów ścieżek wykraczających poza płytkę
- k5 – sumaryczna liczba segmentów ścieżek wykraczających poza płytkę
- w1-5 – wagi poszczególnych parametrów.

Dla tak przyjętej funkcji przystosowania najlepszym rozwiązaniem będzie to o najniższej sumie kar.

### 1.6. Selekcja
Dokonanie selekcji może zostać wykonane za pomocą 2 operatorów: turniej i ruletka.

Operator turniejowy polega na losowym wybraniu T osobników spośród populacji. Następnie spośród wybranych osobników zwrócony zostaje najbardziej przystosowany spośród nich.

Operator ruletki przypisuje każdemu osobnikowi prawdopodobieństwo jego wylosowania na podstawie jego przystosowania. Prawdopodobieństwo wylosowania każdego osobnika jest przeciwnie proporcjonalne do wyniku funkcji oceniającej.

### 1.7. Krzyżowanie
Po wyborze pary osobników, potomkowie mogą powstać poprzez kopiowanie rodziców lub poprzez dokonanie krzyżowania. Krzyżowanie rozwiązania następuje poprzez iterowanie po listach ścieżek obu rodziców. Dla każdej pary ścieżek z odpowiednim prawdopodobieństwem następuje ich wymiana. Tak otrzymujemy parę potomków.

### 1.8. Mutacja
Po dokonaniu selekcji i krzyżowania otrzymane osobniki z pewnym prawdopodobieństwem mogą zostać poddane mutacji. Polega ona na losowym wyborze jednej ścieżki oraz przesunięciu fragmentu losowego segmentu tej ścieżki.

## 2. Źródła
[1] Wiktor Walentynowicz, Artur Zawisza - Algorytm Genetyczny - implementacja i badanie - Laboratorium SI zadanie 1

[2] Arabas J. - Wykłady z algorytmów ewolucyjnych (http://staff.elka.pw.edu.pl/~jarabas/ksiazka.html)

[3] Goldberg D. - Algorytmy genetyczne i ich zastosowanie

[4] Michalewicz Z. - Algorytmy genetyczne + struktury danych = programy ewolucyjne, WNT.

[5] Krunoslav Puljić, Robert Manger - „Comparison of eight evolutionary crossover operators for the vehicle routing problem”, MATHEMATICAL COMMUNICATIONS Math. Commun. 18(2013), 359–375

[6] Potvin, Jean-Yves - 1996. Genetic algorithms for the the traveling salesman problem,Annals of Operations Research, Volume 63, pages 339-370.

[7] http://edu.pjwstk.edu.pl/wyklady/nai/scb/wyklad10/w10.htm

[8] Pasek R. - „Techniki Ewolucyjne w projektowaniu układu ścieżek na płytkach drukowanych”

## 3. Przykładowe dane
```json
{
	"sizeX": 6,
	"sizeY": 6,
	"starts": [
		{
			"x": 1,
			"y": 3
		},
		{
			"x": 3,
			"y": 1
		}
		],
	"ends": [
		{
			"x": 5,
			"y": 3
		},
		{
			"x": 3,
			"y": 3
		}
	],
	"crossingPenalty": 1000,
	"pathLengthPenalty": 20,
	"numberOfSectionsPenalty": 1,
	"pathsOutOfPcbPenalty": 2000,
	"pathsOutOfPcbLengthPenalty": 1500,
	"populationSize": 100,
	"tournamentSize": 10,
	"numberOfGenerations": 1000,
	"crossingProbability": 0.6,
	"swapProbability": 0.2,
	"mutationProbability": 0.2,
	"rouletteSelection": false
}
```
- sizeX - szerokość płytki drukowanej (liczba całkowita - pole obligatoryjne)
- sizeY - wysokość płytki drukowanej (liczba całkowita - pole obligatoryjne)
- starts - lista punktów początkowych (pole obligatoryjne)
- ends - lista punktów końcowych (pole obligatoryjne)
- crossingPenalty - wartość kary funkcji oceniającej za każde przecięcie ścieżek (liczba całkowita - pole opcjonalne)
- pathLengthPenalty - wartość kary funkcji oceniającej za długość ścieżek (liczba całkowita - pole opcjonalne)
- numberOfSectionsPenalty - wartość kary funkcji oceniającej za liczbę segmentów w ścieżkach (liczba całkowita - pole opcjonalne)
- pathsOutOfPcbPenalty - wartość kary funkcji oceniającej za liczbę ścieżek wykraczających poza obszar PCB (liczba całkowita - pole opcjonalne)
- pathsOutOfPcbLengthPenalty - wartość kary funkcji oceniającej za  długość fragmentów ścieżek poza PCB (liczba całkowita - pole opcjonalne)
- populationSize - wielkość populacji (liczba całkowita - pole opcjonalne)
- tournamentSize - wielkość turnieju (liczba całkowita - pole opcjonalne)
- numberOfGenerations - liczba pokoleń (liczba całkowita - pole opcjonalne)
- crossingProbability - prawdopodobieństwo krzyżowania (liczba zmiennoprzecinkowa - pole opcjonalne)
- swapProbability - prawdopodobieństwo zamiany ścieżek podczas krzyżowania (liczba zmiennoprzecinkowa - pole opcjonalne)
- mutationProbability - prawdopodobieństwo mutacji (liczba zmiennoprzecinkowa - pole opcjonalne)
- rouletteSelection - wybór operatora selekcji (boolean - pole opcjonalne) (true - operator ruletki, false (domyślne) operator turniejowy 

## 4. Dostępne endpointy
GET / - strona główna zawierająca tą dokumentację oraz moduł pozwalający przetestować endpoint /pcb

GET /default - zwraca obiekt JSON zawierający domyślne wartości parametrów

POST /pcb - uruchamia algorytm genetyczny dla zadanych parametrów i zwraca uzyskane rozwiązanie

## 5. Opis wywołania
Po uruchomieniu usługi można sprawdzić jej działanie poprzez stronę główną dostępną pod adresem: 
- http://localhost:{port}/ (w przypadku uruchomienia lokalnie)
- https://pcb-path-generator.herokuapp.com (w przypadku korzystania z serwera heroku)

## 6. Uruchomienie kontenera z usługą
Korzystając z Docker CLI obraz może zostać uruchomiony poprzez użycie polecenia:

```docker run -p {port}:8080 damianbartosz/pcb-paths-generator```

## 7. Linki
- nazwa obrazu w repozytorium obrazów
	- damianbartosz/pcb-paths-generator
- lokalizacja przykładowych danych
	- .../data
