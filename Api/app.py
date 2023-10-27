import sys
from flask import Flask, request
import datetime
from flask import Flask
from flask import Blueprint, jsonify


app = Flask("serviceMatch")


@app.route('/healthcheck', methods=['GET'])
def healthcheck():
    return {"message": "i'm alive"}, 200


@app.route('/tipoProfesion', methods=['GET'])
def tipoServicio():
    try:
        profesiones = [	
				'Electricista'
				,'Plomero'
				,'Carpintero'
				,'Albañil'
				,'Mecánico'
				,'Jardinero'
				,'Herrero'
				,'Zapatero'
				,'Costurero'
				,'Pintor'
				,'Gasista'
				,'Limpieza a domicilio'
				,'Técnico en aire acondicionado'
				,'Técnico en refrigeración'
				,'Técnico en computadoras'
				,'Técnico en electrodomésticos'
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

@app.route('/login',methods=['POST'])
def login():
    try:
        data = request.get_json()
        username = data['username']
        password = data['password']

        if username == "serviceMatchUser" and password == "serviceMatch1234":
            return jsonify({'message': 'Login successful'}), 200
        else:
            return jsonify({'message': 'Login error'}), 403
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/zonas', methods=['GET'])
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
        return 500



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
                            'puntajeAsignado': 100
						},
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





if __name__ == '__main__':
    app.run(debug=True)