import random
import time
import cbor2  # Bibliothèque pour encoder en CBOR
import paho.mqtt.client as mqtt  # Bibliothèque MQTT

# Configuration MQTT
SENSOR_ID = "HEART_SENSOR_003"      # Identifiant unique du capteur
BROKER = "mqtt.eclipseprojects.io"  # Broker public
PORT = 1883                         # Port MQTT
TOPIC = "iot/heart_rate/"+SENSOR_ID    # Topic du capteur
TOPIC_STATS ="iot/update/"+SENSOR_ID          # Topic pour les statistiques
TOPIC_live = "iot/heart_rate/chart"+SENSOR_ID    # Topic du capteur



def generate_heart_rate():
    """
    Génère une fréquence cardiaque simulée pour une personne normale.
    :return: Fréquence cardiaque simulée
    """
    return random.randint(60,100)

def generate_heart_rate_average():
    """
    Génère une moyenne de fréquence cardiaque basée sur des mesures toutes les secondes pendant 8 secondes.
    :return: Moyenne de la fréquence cardiaque sur 8 secondes
    """
    heart_rates = []  # Liste pour stocker les valeurs générées

    for _ in range(5):  # Génère une valeur toutes les secondes pendant 5 secondes
        heart_rate = generate_heart_rate()
        heart_rates.append(heart_rate)
        print(f"Valeur instantanée : {heart_rate} BPM")  # Afficher chaque valeur générée
        time.sleep(1)  # Pause d'1 seconde entre chaque mesure

    # Calculer et retourner la moyenne des fréquences cardiaques
    average_heart_rate = sum(heart_rates) / len(heart_rates)
    print(f"Moyenne sur 5 secondes : {average_heart_rate:.2f} BPM\n")
    return average_heart_rate

def create_senml_record(heart_rate):
    """
    Crée une structure SenML avec les données de fréquence cardiaque.
    :param heart_rate: Valeur de fréquence cardiaque simulée
    :return: Un dictionnaire représentant un enregistrement SenML
    """
    return {
        "bn": SENSOR_ID,               # Base Name (identifiant du capteur)
        "bt": time.time(),             # Base Time (timestamp)
        "e": [                         # List of measurements
            {
                "n": "heart_rate",     # Nom du paramètre mesuré
                "u": "BPM",            # Unité (Battements Par Minute)
                "v": heart_rate        # Valeur mesurée
            }
        ]
    }

def encode_cbor(data):
    """
    Encode les données en format CBOR.
    :param data: Données à encoder
    :return: Données encodées en CBOR
    """
    return cbor2.dumps(data)


def calculate_statistics(data_points):
    """
    Calcule la moyenne, le minimum et le maximum d'une liste de données.
    :param data_points: Liste des valeurs de fréquence cardiaque
    :return: Un dictionnaire contenant les statistiques
    """
    return {
        "average": sum(data_points) / len(data_points),
        "min": min(data_points),
        "max": max(data_points)
    }

def simulate_heart_rate():
    """
    Simule un capteur de fréquence cardiaque avec encodage en CBOR et format SenML, 
    et envoie les données au broker MQTT.
    """
    # Initialiser le client MQTT
    client = mqtt.Client(SENSOR_ID)
    client.connect(BROKER, PORT)
    print(f"Capteur connecté au broker MQTT {BROKER}:{PORT}")

        # Variables pour gérer les statistiques
    heart_rate_data = []  # Stocke les valeurs pour calculer les statistiques
    last_stats_time = time.time()
    
    while True:
        # Générer une fréquence cardiaque simulée
        average_heart_rate = generate_heart_rate_average()
        
        # Créer une structure SenML
        senml_data = create_senml_record(average_heart_rate)
        
        # Encoder en CBOR
        cbor_encoded_data = encode_cbor(senml_data)
        
        # Publier les données sur le broker MQTT
        client.publish(TOPIC, cbor_encoded_data)
        client.publish(TOPIC_live,cbor_encoded_data)
        print(f"Données publiées sur {TOPIC} : {senml_data}\n")
        
  # Ajouter la donnée à la liste des statistiques
        heart_rate_data.append(average_heart_rate)

        # Vérifier si une minute s'est écoulée pour publier les statistiques
        current_time = time.time()
        if current_time - last_stats_time >= 60:  # 1 minute
            stats = calculate_statistics(heart_rate_data)
            stats_data = {
                "bn": SENSOR_ID,
                "bt": last_stats_time,  # Timestamp de début de la période
                "e": [
                    {"n": "average_heart_rate", "u": "BPM", "v": stats["average"]},
                    {"n": "min_heart_rate", "u": "BPM", "v": stats["min"]},
                    {"n": "max_heart_rate", "u": "BPM", "v": stats["max"]}
                ]
            }
            client.publish(TOPIC_STATS, encode_cbor(stats_data))
            print(f"Statistiques publiées sur {TOPIC_STATS} : {stats_data}")
            
            
            # Réinitialiser les statistiques
            heart_rate_data = []
            last_stats_time = current_time
        



        # Pause avant la prochaine simulation
        time.sleep(1)

# Lancer la simulation
if __name__ == "__main__":
    simulate_heart_rate()
