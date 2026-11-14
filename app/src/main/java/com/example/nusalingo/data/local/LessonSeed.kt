package com.example.nusalingo.data.local

object LessonSeed {

    private fun item(
        id: Int,
        lesson: String,
        section: String,
        glyph: String,
        latin: String,
        audio: String
    ) = CharacterEntity(
        id = id,
        language = "Jawa",
        category = "Aksara Dasar",
        lesson = lesson,
        section = section,
        glyph = glyph,
        latin = latin,
        audioName = audio
    )
    const val PASANGAN_BENTUK = "BENTUK AKSARA PASANGAN"
    const val PASANGAN_CONTOH = "CONTOH PENGGUNAAN PASANGAN"
    const val SWARA_BENTUK = "BENTUK SANDHANGAN SWARA"
    const val SWARA_CONTOH = "CONTOH PENGGUNAAN SWARA"
    const val PANYIGEG_BENTUK = "BENTUK SANDHANGAN PANYIGEG"
    const val PANYIGEG_CONTOH = "CONTOH PENGGUNAAN PANYIGEG"
    const val KATA_HARI = "KATA 1: HARI"
    const val KATA_BIBI = "KATA 2: BIBI"
    const val KATA_BUKU = "KATA 3: BUKU"
    const val KATA_SAPI = "KATA 4: SAPI"

    fun all(): List<CharacterEntity> {
        val result = mutableListOf<CharacterEntity>()

        val carakan1 = listOf(
            "ꦲ" to "ha", "ꦤ" to "na", "ꦕ" to "ca", "ꦫ" to "ra", "ꦏ" to "ka",
            "ꦢ" to "da", "ꦠ" to "ta", "ꦱ" to "sa", "ꦮ" to "wa", "ꦭ" to "la"
        )
        carakan1.forEachIndexed { i, (g, l) ->
            result.add(item(i + 1, "Carakan 1", "AKSARA CARAKAN (nglegena)", g, l, "aksara_$l"))
        }

        val carakan2 = listOf(
            "ꦥ" to "pa", "ꦝ" to "dha", "ꦗ" to "ja", "ꦪ" to "ya", "ꦚ" to "nya",
            "ꦩ" to "ma", "ꦒ" to "ga", "ꦧ" to "ba", "ꦛ" to "tha", "ꦔ" to "nga"
        )
        carakan2.forEachIndexed { i, (g, l) ->
            result.add(item(i + 11, "Carakan 2", "AKSARA CARAKAN (nglegena)", g, l, "aksara_$l"))
        }

        val pasangan = listOf(
            "꧀ꦲ" to "h", "꧀ꦤ" to "n", "꧀ꦕ" to "c", "꧀ꦫ" to "r", "꧀ꦏ" to "k",
            "꧀ꦢ" to "d", "꧀ꦠ" to "t", "꧀ꦱ" to "s", "꧀ꦮ" to "w", "꧀ꦭ" to "l",
            "꧀ꦥ" to "p", "꧀ꦝ" to "dh", "꧀ꦗ" to "j", "꧀ꦪ" to "y", "꧀ꦚ" to "ny",
            "꧀ꦩ" to "m", "꧀ꦒ" to "g", "꧀ꦧ" to "b", "꧀ꦛ" to "th", "꧀ꦔ" to "ng"
        )
        pasangan.forEachIndexed { i, (g, l) ->
            result.add(item(i + 101, "Carakan 3", PASANGAN_BENTUK, g, l, "pasangan_$l"))
        }
        val contohPasangan = listOf(
            Triple("ꦩꦸꦤ꧀ꦠꦁ", "muntang", "kata_muntang"),
            Triple("꧀ꦠ", "pasangan ta", "pasangan_t"),
            Triple("ꦤꦏ꧀ꦧꦂ", "anak band", "kata_anak_band"),
            Triple("꧀ꦧ", "pasangan ba", "pasangan_b")
        )
        contohPasangan.forEachIndexed { i, (g, l, a) ->
            result.add(item(i + 131, "Carakan 3", PASANGAN_CONTOH, g, l, a))
        }

        val swaraBentuk = listOf(
            Triple("◌ꦶ", "wulu (i)", "sandhangan_wulu"),
            Triple("◌ꦸ", "suku (u)", "sandhangan_suku"),
            Triple("◌ꦼ", "pepet (ê)", "sandhangan_pepet"),
            Triple("◌ꦺ", "taling (e)", "sandhangan_taling"),
            Triple("◌ꦺꦴ", "taling tarung (o)", "sandhangan_taling_tarung")
        )
        swaraBentuk.forEachIndexed { i, (g, l, a) ->
            result.add(item(i + 201, "Sandhangan Swara", SWARA_BENTUK, g, l, a))
        }
        val swaraContoh = listOf(
            Triple("ꦏꦶ", "ki", "swara_ki"),
            Triple("ꦏꦸ", "ku", "swara_ku"),
            Triple("ꦏꦼ", "kê", "swara_ke_pepet"),
            Triple("ꦏꦺ", "ke", "swara_ke_taling"),
            Triple("ꦏꦺꦴ", "ko", "swara_ko")
        )
        swaraContoh.forEachIndexed { i, (g, l, a) ->
            result.add(item(i + 211, "Sandhangan Swara", SWARA_CONTOH, g, l, a))
        }

        val panyigegBentuk = listOf(
            Triple("◌ꦁ", "cecak (ng)", "panyigeg_cecak"),
            Triple("◌ꦃ", "wignyan (h)", "panyigeg_wignyan"),
            Triple("◌ꦂ", "layar (r)", "panyigeg_layar"),
            Triple("◌꧀", "pangkon (mati)", "panyigeg_pangkon")
        )
        panyigegBentuk.forEachIndexed { i, (g, l, a) ->
            result.add(item(i + 301, "Sandhangan Panyigeg Wanda", PANYIGEG_BENTUK, g, l, a))
        }
        val panyigegContoh = listOf(
            Triple("ꦏꦁ", "kang", "panyigeg_kang"),
            Triple("ꦏꦃ", "kah", "panyigeg_kah"),
            Triple("ꦏꦂ", "kar", "panyigeg_kar"),
            Triple("ꦏ꧀", "k", "panyigeg_k")
        )
        panyigegContoh.forEachIndexed { i, (g, l, a) ->
            result.add(item(i + 311, "Sandhangan Panyigeg Wanda", PANYIGEG_CONTOH, g, l, a))
        }

        val hari = listOf(
            Triple("ꦲ", "ha", "aksara_ha"),
            Triple("ꦲꦫ", "hara", "kata_hara"),
            Triple("ꦲꦫꦶ", "hari", "kata_hari")
        )
        hari.forEachIndexed { i, (g, l, a) ->
            result.add(item(i + 401, "Merangkai Kata", KATA_HARI, g, l, a))
        }
        val bibi = listOf(
            Triple("ꦧ", "ba", "aksara_ba"),
            Triple("ꦧꦶ", "bi", "kata_bi"),
            Triple("ꦧꦶꦧ", "biba", "kata_biba"),
            Triple("ꦧꦶꦧꦶ", "bibi", "kata_bibi")
        )
        bibi.forEachIndexed { i, (g, l, a) ->
            result.add(item(i + 411, "Merangkai Kata", KATA_BIBI, g, l, a))
        }
        val buku = listOf(
            Triple("ꦧ", "ba", "aksara_ba"),
            Triple("ꦧꦸ", "bu", "kata_bu"),
            Triple("ꦧꦸꦏ", "buka", "kata_buka"),
            Triple("ꦧꦸꦏꦸ", "buku", "kata_buku")
        )
        buku.forEachIndexed { i, (g, l, a) ->
            result.add(item(i + 421, "Merangkai Kata", KATA_BUKU, g, l, a))
        }
        val sapi = listOf(
            Triple("ꦱ", "sa", "aksara_sa"),
            Triple("ꦱꦥ", "sapa", "kata_sapa"),
            Triple("ꦱꦥꦶ", "sapi", "kata_sapi")
        )
        sapi.forEachIndexed { i, (g, l, a) ->
            result.add(item(i + 431, "Merangkai Kata", KATA_SAPI, g, l, a))
        }

        return result
    }
}