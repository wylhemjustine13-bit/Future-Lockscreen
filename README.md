# FUTURE LOCKSCREEN — prototype Android 0.1

Premier prototype natif Android en Kotlin + Jetpack Compose.

## Ce que teste cette version
- Affiche l'heure réelle au format 24 h, sans deux-points.
- Garde l'écran allumé tant que l'application est ouverte au premier plan.
- Permet de simuler une période d'inactivité (30 secondes pour le test, 1 h ou 2 h).
- Passe ensuite à un affichage très assombri.
- Un double toucher sur l'écran réveille l'affichage dans l'application.

## Limites connues
Cette version ne remplace pas l'écran de verrouillage Samsung et ne peut pas réveiller un écran complètement éteint. Le double toucher est détecté uniquement lorsque l'activité de l'application est encore affichée. Le maintien de l'écran allumé augmente la consommation de batterie.

## Construire l'APK
1. Ouvrir ce dépôt dans Android Studio (Open).
2. Attendre la synchronisation Gradle.
3. Choisir **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. Installer l'APK de test sur un appareil Android.

Le délai de 30 secondes sert uniquement à vérifier rapidement le passage en mode veille. Les délais d'une et deux heures sont disponibles pour les essais réels.
