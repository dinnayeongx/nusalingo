package com.example.nusalingo.feature.lesson.ui
import com.example.nusalingo.data.local.LessonSeed
val lessonTitles = listOf(
    "Carakan 1",
    "Carakan 2",
    "Carakan 3",
    "Sandhangan Swara",
    "Sandhangan Panyigeg Wanda",
    "Merangkai Kata"
)
fun lessonDescription(title: String): String = when (title) {
    "Carakan 1" -> "Mengenal 10 aksara dasar pertama: ha, na, ca, ra, ka, da, ta, sa, wa, la."
    "Carakan 2" -> "Melengkapi 20 aksara dasar: pa, dha, ja, ya, nya, ma, ga, ba, tha, nga."
    "Carakan 3" -> "Aksara pasangan dipakai untuk mematikan vokal aksara di depannya."
    "Sandhangan Swara" -> "Tanda untuk mengubah bunyi vokal aksara menjadi i, u, ê, e, dan o."
    "Sandhangan Panyigeg Wanda" -> "Tanda penutup suku kata: cecak (ng), wignyan (h), layar (r), dan pangkon."
    "Merangkai Kata" -> "Latihan menggabungkan aksara dan sandhangan menjadi kata utuh."
    else -> ""
}
fun sectionNote(sectionTitle: String): String = when (sectionTitle) {
    LessonSeed.PASANGAN_BENTUK ->
        "Aksara pasangan adalah bentuk khusus dari aksara dasar yang berfungsi mematikan " +
                "atau menghilangkan vokal /a/ pada aksara dasar di depannya.\n\n" +
                "Fungsi utama: menuliskan huruf konsonan mati di tengah kata, menyambung suku kata " +
                "tertutup, atau penulisan antar-gatra kalimat agar tidak rancu.\n\n" +
                "Setiap dari 20 aksara dasar memiliki pasangannya sendiri. Contohnya Pasangan Ha " +
                "mematikan vokal menjadi konsonan h, Pasangan Na menjadi n, dan seterusnya."
    LessonSeed.PASANGAN_CONTOH ->
        "Posisi pasangan ada yang diletakkan di bawah aksara dasar, ada pula yang sejajar " +
                "di samping kanan aksara dasar, tergantung bentuk karakternya.\n\n" +
                "Contoh 1: kata \"Muntang\" memakai pasangan Ta. Terdiri dari aksara Ma + suku, " +
                "aksara Na yang dimatikan dengan pasangan Ta, lalu cecak.\n\n" +
                "Contoh 2: kata \"Anak Band\" memakai pasangan Ba. Terdiri dari aksara Na, aksara Ka " +
                "yang dimatikan dengan pasangan Ba, lalu layar."
    LessonSeed.SWARA_BENTUK ->
        "Sandhangan swara mengubah bunyi vokal /a/ pada aksara dasar. " +
                "Wulu menjadi i, suku menjadi u, pepet menjadi ê (seperti pada kata \"segar\"), " +
                "taling menjadi e (seperti pada \"sate\"), dan taling tarung menjadi o. " +
                "Tanda ◌ menunjukkan tempat aksara dasar."
    LessonSeed.SWARA_CONTOH ->
        "Semua sandhangan dipasangkan dengan aksara dasar Ka (ꦏ). " +
                "Perhatikan bagaimana bunyi \"ka\" berubah sesuai sandhangan yang ditambahkan."
    LessonSeed.PANYIGEG_BENTUK ->
        "Sandhangan panyigeg wanda menutup suku kata dengan konsonan. " +
                "Cecak menutup dengan \"ng\", wignyan dengan \"h\", dan layar dengan \"r\". " +
                "Pangkon mematikan vokal /a/ pada aksara di akhir kata."
    LessonSeed.PANYIGEG_CONTOH ->
        "Semua sandhangan dipasangkan dengan aksara dasar Ka (ꦏ). " +
                "Bunyi \"ka\" berubah menjadi kang, kah, kar, atau k."
    LessonSeed.KATA_HARI ->
        "Langkah 1: tulis aksara dasar Ha (ꦲ) untuk bunyi awal \"ha\".\n" +
                "Langkah 2: tambahkan aksara dasar Ra (ꦫ) di sebelah kanan untuk bunyi \"ra\".\n" +
                "Langkah 3: beri sandhangan Wulu (ꦶ) tepat di atas Ra agar vokal /a/ berubah menjadi /i/ (ri).\n" +
                "Hasil akhir: kata Hari (ꦲꦫꦶ)."
    LessonSeed.KATA_BIBI ->
        "Langkah 1: tulis aksara dasar Ba (ꦧ) untuk suku kata pertama.\n" +
                "Langkah 2: tambahkan sandhangan Wulu (ꦶ) di atas Ba agar berbunyi \"bi\".\n" +
                "Langkah 3: tulis kembali Ba (ꦧ) di sebelah kanannya untuk suku kata kedua.\n" +
                "Langkah 4: tambahkan lagi Wulu (ꦶ) di atas Ba kedua agar berbunyi \"bi\".\n" +
                "Hasil akhir: kata Bibi (ꦧꦶꦧꦶ)."
    LessonSeed.KATA_BUKU ->
        "Langkah 1: tulis aksara dasar Ba (ꦧ) sebagai suku kata awal.\n" +
                "Langkah 2: tambahkan sandhangan Suku (ꦸ) di bagian bawah Ba untuk mengubah /a/ menjadi /u/ (bu).\n" +
                "Langkah 3: tulis aksara dasar Ka (ꦏ) di sebelah kanannya.\n" +
                "Langkah 4: tambahkan sandhangan Suku (ꦸ) di bawah Ka agar berbunyi \"ku\".\n" +
                "Hasil akhir: kata Buku (ꦧꦸꦏꦸ)."
    LessonSeed.KATA_SAPI ->
        "Langkah 1: tulis aksara dasar Sa (ꦱ) untuk bunyi awal \"sa\".\n" +
                "Langkah 2: tulis aksara dasar Pa (ꦥ) di sebelah kanannya untuk bunyi \"pa\".\n" +
                "Langkah 3: tambahkan sandhangan Wulu (ꦶ) di atas Pa agar bunyinya berubah dari pa menjadi \"pi\".\n" +
                "Hasil akhir: kata Sapi (ꦱꦥꦶ)."
    else -> ""
}