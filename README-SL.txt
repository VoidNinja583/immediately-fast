IMMEDIATELY_FAST — Fabric 1.21.1

Kaj vsebuje:
- Right Shift odpre nastavitveni meni.
- Safe Anchor Helper: v Overworldu blokira uporabo respawn anchorja, če totema ni v glavni ali drugi roki.
- Anchor/Glowstone/Totem Switch: ob vklopu preklaplja izbrano hotbar režo med anchorjem, glowstoneom in totemom, če so vsi ti predmeti v hotbaru.
- AutoInv Totem: v prazno drugo roko prestavi totem iz inventarja; če je izbrana glavna roka prazna, lahko totem prestavi tudi tja.
- Vsaka funkcija ima ON/OFF. Zakasnitvi se spreminjata s klikanjem na ustrezen gumb (1–20 tickov).

Pomembne omejitve:
- Switch ne izvaja samodejnega postavljanja anchorja ali uporabe glowstonea; samo izbira ustrezne hotbar reže.
- AutoInv Totem ne prepiše predmeta, ki je že v drugi roki.
- Samodejne menjave inventarja so odvisne od strežnika in lahko na nekaterih strežnikih niso dovoljene.
- Projekt je izvorna koda; .jar ni bil zgrajen ali preizkušen v tem okolju.

Kako zgradiš .jar:
1. Namesti Java JDK 21.
2. Namesti Gradle 8.x ali odpri mapo v IDE, ki podpira Gradle.
3. V mapi projekta zaženi: gradle build
4. .jar bo v build/libs/immediately_fast-1.0.0.jar
5. Datoteko kopiraj v mapo .minecraft/mods. Potrebuješ Fabric Loader in Fabric API za Minecraft 1.21.1.

Če Gradle javi napako zaradi različice Loom ali odvisnosti, preveri uradne Fabric dokumente za združljivo različico.
