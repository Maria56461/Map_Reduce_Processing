# Map_Reduce_Processing
Parallel program that processes a list of documents using the Map-Reduce Paradigm and orders the documents by their calculated rank.

##Maria-Alexandra Barbu, 335CA
##Procesarea de documente folosind paradigma Map-Reduce
-------------------------------------------------------------------------------

Am citit linie cu linie datele din fișierul de intrare folosind clasa
Scanner. Am creat un vector cu toate fișierele ce urmează a fi procesate.
Într-o buclă, am parcurs fiecare fișier din ArrayList-ul creat. Am stocat
textul întreg din fiecare fișier într-un element de HashMap de tipul
<File, StringBuilder>. Am citit toate fișierele date caracter cu caracter
folosind clasa FileReader. Am construit un vector de String-uri în care am pus
fragmentele de câte "fragDim" octeți formate plus ultimul fragment, format din
restul de caractere. Am creat apoi o clasă reprezentativă pentru un obiect de
tip Task și în funcția "main" am alocat memorie pentru un vector de taskuri. Un
Task conține un fragment de text, numele fișierului din care acesta face parte,
dimensiunea fragmentului și offset-ul de la care începe acesta. Pentru
thread-uri am creat o clasă numită "Worker". Fiecare Worker are, printre
altele, un vector de taskuri pe care trebuie să le rezolve și un vector de
rezultate ale taskurilor. De asemenea, un Worker are acces la varianta completă
a textelor din fișierele procesate. Taskurile sunt distribuite workerilor în
mod echilibrat folosind formulele pentru "start" și "end" pe care le-am învățat
la laborator. Clasa "Worker" moștenește clasa Thread și suprascrie metoda
"run". În metoda "run", worker-ul parcurge vectorul de taskuri, accesează
textul complet din fișierul taskului curent, apoi merge la offset-ul specificat
în task. Dacă ultimul caracter din fragment și încă unul mai la dreapta sunt
caractere alfanumerice, atunci fragmentul se termină în mijlocul unui cuvânt,
deci vom parcurge câteva caractere spre dreapta până când descoperim un
separator, iar caracterelor parcurse le vom face "append" la fragmentul curent.
Dacă primul caracter din fragment și încă unul mai înapoi sunt caractere
alfanumerice, atunci fragmentul începe în mijlocul unui cuvânt, deci vom shifta
întregul fragment mai la stânga și vom șterge ultimele caractere. Dacă trebuie
să alipim întregul fragment curent la cel anterior, vom șterge cu totul textul
task-ului curent. Mai departe, worker-ul va împărți fragmentul curent în
cuvinte folosind metoda "split". Parcurgând fiecare cuvânt, acesta va stoca
într-un HashMap numărul de cuvinte având o anumită lungime și într-o listă va
pune toate cuvintele de lungime maximă din fragment. Toate rezultatele
taskurilor vor fi stocate în obiecte de tip "Dex" și accesate în funcția "main"
după ce se dă join thread-urilor prin intermediul unui Getter. Am creat apoi al
doilea tip de task-uri, obiecte Task2, câte un task pentru fiecare fișier
existent, și un al doilea tip de workeri care să rezolve aceste taskuri. Un
worker va itera prin vectorul de HashMap-uri al fiecărui task și va calcula
rangul fișierului corespunzător taskului curent. Rezultatul unui Task2 va fi
stocat într-un obiect de tip Result. Toate rezultatele vor fi accesate în
"main" și vor fi scrise în fișierul de ieșire specificat la rulare. În final,
am sortat fișierele descrescător după rang folosind un Comparator și clasa
Collections.

-------------------------------------------------------------------------------
