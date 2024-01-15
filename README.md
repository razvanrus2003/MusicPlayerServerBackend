# Proiect GlobalWaves - Etapa 3

<div align="center"><img src="https://tenor.com/view/listening-to-music-spongebob-gif-8009182.gif" width="300px"></div>

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa1](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa1)

# Design Patterns

Creational Patterns: Library este implementat ca si Singleton;\
Behavioural Patterns: Executia comenzilor este implementata ca si Strategy deoarece este foarte asemanator cu Pattern-ul
Command; Notificarea utilizatorilor sunt implementate ca si Observer; Sistemul de pagini este implementat ca si Command, fiecare user avand un istoric de PageCommand-uri;

# Citire

Clasa Command este extinsa in mai multe subclase ce supreasriu metoda execute
intr-un mod specific fiecarei comenzi.
Citesc din fisierul de input un vector de obiecte Command, care cu ajutorul
adnotarilor JSON o sa fie defapt instante specifice comenzii care este data spre executie.\
Apoi dau execut fiecare comanda in parte. Se intoarce un obiect de tip CommandOuput sau o derivata a acestuia care este
adaugata la output.

# Structura Claselor

Clasele Command, CommandOutput si Item sunt derivate in diverse clase pentru a folosi suprascrierea metodelor si vectori
de tipuri variate.\
Spre exemplu: folosind metoda validate(sprascrisa in Song, Playlist si Podcast) se poate determina cu usurinta
rezulutatele unei operatii de cautare conform filtrelor specificate.\
Clase Library este de tip Singleton cu implentare Lazy deoarece este necesara o singura instanta pe tot parcursul
programului.

# Executia Programului
Comenzile sunt citite din fisierul de input sunt traduse in obiecte de tip Command si executate.
La inceputul executiei unei comenzi se obtine un obiect de tip User cu username-ul specificat in comanda, folosint metoda getUser din Library.
Din acest obiect se poate accesa orice informatie necesara pentru executia comenzii.
La finalul executiei, comenzii generaza un obiect de tip CommandOutput, pe care il returneaza, acesta este special construit pentru a fi tradus in format json si adaugat la output.
\
Programul nu retine niciun fel de queue pentru melodii doar sursa redarii si pozitia melodiei curente in aceasta.
Pentru ad-uri se retin doar cantecele care au fost deja redate de la ultimul ad si monetizarea se face cand un nou ad este redat.



