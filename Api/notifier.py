import time
from firebase_admin import messaging
import firebase_admin
from firebase_admin import credentials

def init_FCM(retries = 5):
    for i in range(1,retries):
        try:
            cred = credentials.Certificate("token-mobile.json")
            firebase_admin.initialize_app(cred)
            return
        except Exception as e:
            if i < retries:
                time.sleep(2)
            else:
                raise Exception(f"No se pudo conectar con FCM: {str(e)}")

def send_notification(body, id_proveedor, precio, disponibilidad, token):
    message = messaging.Message(
        notification = messaging.Notification (
            title = "ServiMatch",
            body  = body
        ),
        data = {
            'id_proveedor': str(id_proveedor),
            'price': precio,
            'disponibilidad': disponibilidad
        },
        token = token 
    )
    response = messaging.send(message)
    print(f"La notificación fue enviada correctamente: {response}")

