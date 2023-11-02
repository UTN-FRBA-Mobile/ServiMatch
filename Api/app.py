import json
import math
import sys
import datetime
from flask import Blueprint, jsonify, Flask, request
from dummy_data import PROVIDERS

app = Flask("serviceMatch")


@app.route('/healthcheck', methods=['GET'])
def healthcheck():
    return {"message": "i'm alive"}, 200


@app.route('/tipoProfesion', methods=['GET'])
def tipoServicio():
    try:
        profesiones = [
            'Electricista'
            , 'Plomero'
            , 'Carpintero'
            , 'Albañil'
            , 'Mecánico'
            , 'Jardinero'
            , 'Herrero'
            , 'Zapatero'
            , 'Costurero'
            , 'Pintor'
            , 'Gasista'
            , 'Limpieza a domicilio'
            , 'Técnico en aire acondicionado'
            , 'Técnico en refrigeración'
            , 'Técnico en computadoras'
            , 'Técnico en electrodomésticos'
        ]

        return jsonify(profesiones), 200
    except Exception as e:
        return {'error': str(e)}, 500


@app.route('/valoraciones', methods=['GET'])
def valoraciones():
    try:
        valoracion = [
            '0', '1', '1.5', '2', '2.5', '3', '3.5', '4', '4.5', '5',
            '5.5', '6', '6.5', '7', '7.5', '8', '8.5', '9', '9.5', '10'
        ]

        return jsonify(valoracion), 200
    except Exception as e:
        return {'error': str(e)}, 500


@app.route('/login', methods=['POST'])
def login():
    try:
        data = request.get_json()
        username = data['username']
        password = data['password']

        if username == "admin" and password == "admin":
            return jsonify({'message': 'Login successful'}), 200
        else:
            return jsonify({'message': 'Login error'}), 403
    except Exception as e:
        return jsonify({'error': str(e)}), 500


""" @app.route('/zonas', methods=['GET'])
def zonas():
    try:
        zonas = [	
            'Recoleta'
            ,'Palermo'
            ,'San Telmo'
            ,'La Boca  '
            ,'Microcentro'
            ,'Belgrano'
            ,'Caballito'
            ,'Boedo'
            ,'Almagro'
            ,'Villa Crespo'
            ,'Villa Devoto'
            ,'Villa del Parque'
            ,'Núñez'
            ,'Saavedra'
            ,'Flores'
            ,'Villa Lugano'
            ,'Mataderos'
            ,'Parque Chacabuco'
            ,'Liniers'
            ,'Villa Urquiza'
            ,'Coghlan'
            ,'Villa Pueyrredón'
            ,'Balvanera'
            ,'Barracas'
            ,'Villa Riachuelo'
            ,'Parque Patricios'
            ,'Versalles'
            ,'Villa Luro'
            ,'Villa Santa Rita'
            ,'Villa Ortúzar'
            ,'Parque Avellaneda'
            ,'Villa General Mitre'
            ,'Villa Soldati'
            ,'Monte Castro'
            ,'Villa Real	'
            ,'Vélez Sársfield'
            ,'Villa Riachuelo'
            ,'Córdoba'
            ,'Rosario'
            ,'Mendoza'
            ,'Tucumán'
            ,'La Plata'
            ,'Mar del Plata'
            ,'Salta'
            ,'Santa Fe'
            ,'San Juan'
            ,'San Miguel de Tucumán'
            ,'San Luis'
            ,'Bariloche'
            ,'Bahía Blanca'
            ,'Neuquén'
            ,'Posadas'
            ,'Corrientes'
            ,'Resistencia'
            ,'San Salvador de Jujuy'
            ,'Formosa'
            ,'Santa Rosa'
            ,'Paraná'
            ,'Ushuaia'
            ,'Comodoro Rivadavia'
            ,'La Rioja'
            ,'Rawson'
            ,'Catamarca'
            ,'Santiago del Estero'
            ,'San Fernando del Valle de Catamarca'
            ,'Río Gallegos'
            ,'Viedma'
        ]	
        return jsonify(zonas),200
    except Exception as e:
        return "Error", 500 """


@app.route('/recomendados', methods=['GET'])
def recomendados():
    try:
        recomendados = [{
            "id": 0,
            "path": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQF5TcVFjPc_Z0ZdLUAA2Df6uTrJL1C5Al4-w&usqp=CAU",
            "nombre": "Joaquin Benitez",
            "precioCategoria": "$$$$",
            "ubicación": "Palermo",
            "rol": "plomero"
        }]
        return jsonify(recomendados), 200
    except Exception as e:
        return "Error", 500


@app.route('/usuarios', methods=['GET'])
def comentarios():
    try:
        comentarios = {
            'usuario1': {
                'name': 'Fernando',
                'lastname': 'Rabinovich',
                'profesion': 'profesion',
                'puntajePromedio': 800,
                'comentarios': {
                    'comentario1': {
                        'comentarioId': 1,
                        'mensaje': 'Mensaje 1',
                        'puntajeAsignado': 100
                    },
                    'comentario2': {
                        'comentarioId': 2,
                        'mensaje': 'Mensaje 2',
                        'puntajeAsignado': 100
                    }
                }
            },
            'usuario2': {
                'name': 'Pedro',
                'lastname': 'Bruno',
                'profesion': 'profesion',
                'puntajePromedio': 800,
                'comentarios': {
                    'comentario1': {
                        'comentarioId': 1,
                        'mensaje': 'Mensaje 1',
                        'puntajeAsignado': 100
                    },
                    'comentario2': {
                        'comentarioId': 2,
                        'mensaje': 'Mensaje 2',
                        'puntajeAsignado': 100
                    }
                }
            },
            'usuario3': {
                'name': 'Hugo',
                'lastname': 'Peykovick',
                'profesion': 'profesion',
                'puntajePromedio': 800,
                'comentarios': {
                    'comentario1': {
                        'comentarioId': 1,
                        'mensaje': 'Mensaje 1',
                        'puntajeAsignado': 100
                    },
                    'comentario2': {
                        'comentarioId': 2,
                        'mensaje': 'Mensaje 2',
                        'puntajeAsignado': 100
                    }
                }
            },
            'usuario4': {
                'name': 'Agustin',
                'lastname': 'Saturni',
                'profesion': 'profesion',
                'puntajePromedio': 800,
                'comentarios': {
                    'comentario1': {
                        'comentarioId': 1,
                        'mensaje': 'Mensaje 1',
                        'puntajeAsignado': 100},
                    'comentario2': {
                        'comentarioId': 2,
                        'mensaje': 'Mensaje 2',
                        'puntajeAsignado': 100
                    }
                }
            },
            'usuario5': {
                'name': 'Nicolas',
                'lastname': 'Fuentes',
                'profesion': 'profesion',
                'puntajePromedio': 800,
                'comentarios': {
                    'comentario1': {
                        'comentarioId': 1,
                        'mensaje': 'Mensaje 1',
                        'puntajeAsignado': 100
                    },
                    'comentario2': {
                        'comentarioId': 2,
                        'mensaje': 'Mensaje 2',
                        'puntajeAsignado': 100
                    }
                }
            }
        }
        return comentarios, 200
    except Exception as e:
        return {'error': str(e)}, 500


@app.route('/providers/', methods=['GET'])
def providers():
    try:
        return jsonify(PROVIDERS), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500


def filterByDistancia(proveedor, latitudCliente, longitudCliente):
    latitudProveedor = proveedor['latitud']
    longitudProveedor = proveedor['longitud']
    rangoMaxProveedor = proveedor['rangoMax']
    if (rangoMaxProveedor > distancia(latitudCliente, longitudCliente, latitudProveedor, longitudProveedor)):
        return True
    else:
        return False

def distancia(lat1, lon1, lat2, lon2):
    R = 6371  # Radio de la Tierra en kilómetros
    dLat = math.radians(lat2 - lat1)
    dLon = math.radians(lon2 - lon1)
    a = math.sin(dLat / 2) * math.sin(dLat / 2) + math.cos(math.radians(lat1)) * math.cos(
        math.radians(lat2)) * math.sin(dLon / 2) * math.sin(dLon / 2)
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    distancia = R * c * 1000  # Distancia en metros
    return distancia

@app.route('/providersByCoordinates', methods=['GET'])
def getProvidersByCoordinates():
    try:
        latitudCliente = float(request.args.get('latitud'))
        longitudCliente = float(request.args.get('longitud'))
        filterFunc = list(filter(lambda x: filterByDistancia(x, latitudCliente, longitudCliente), PROVIDERS))
        return jsonify(filterFunc), 200
    except Exception as e:
        return {'error': str(e)}, 500

@app.route('/providers/<int:provider_id>', methods=['GET'])
def provider_profile(provider_id):
    provider = next((provider for provider in PROVIDERS if provider["id"] == provider_id), None)
    if provider is not None:
        return jsonify(provider), 200
    return jsonify({'error': f"no se pudo encontrar el proveedor {provider_id}"}), 500

if __name__ == '__main__':
    app.run(debug=True)
