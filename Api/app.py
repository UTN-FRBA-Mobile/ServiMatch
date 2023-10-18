import sys
from flask import Flask
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
            'plomeria',
            'gasista',
            'techista',
            'envios'
        ]

        return jsonify(profesiones), 200
    except Exception as e:
        return {'error': str(e)}, 500


@app.route('/usuarios', methods=['GET'])
def comentarios():
    try:
        comentarios = {
				'usuario1': {
					'name': 'Fernando',
					'lastname': 'Rabinovich',
					'profesion': 'profesion',
					'comentarios': {
						'comentario1': {
							'comentarioId': 1,
							'mensaje': 'Mensaje 1'
						},
						'comentario2': {
							'comentarioId': 2,
							'mensaje': 'Mensaje 2'
						}
					}
				},
				'usuario2': {
					'name': 'Pedro',
					'lastname': 'Bruno',
					'profesion': 'profesion',
					'comentarios': {
						'comentario1': {
							'comentarioId': 1,
							'mensaje': 'Mensaje 1'
						},
						'comentario2': {
							'comentarioId': 2,
							'mensaje': 'Mensaje 2'
						}
					}
				},
				'usuario3': {
					'name': 'Hugo',
					'lastname': 'Peykovick',
					'profesion': 'profesion',
					'comentarios': {
						'comentario1': {
							'comentarioId': 1,
							'mensaje': 'Mensaje 1'
						},
						'comentario2': {
							'comentarioId': 2,
							'mensaje': 'Mensaje 2'
						}
					}
				},
				'usuario4': {
					'name': 'Agustin',
					'lastname': 'Saturni',
					'profesion': 'profesion',
					'comentarios': {
						'comentario1': {
							'comentarioId': 1,
							'mensaje': 'Mensaje 1'
						},
						'comentario2': {
							'comentarioId': 2,
							'mensaje': 'Mensaje 2'
						}
					}
				},
				'usuario5': {
					'name': 'Nicolas',
					'lastname': 'Fuentes',
					'profesion': 'profesion',
					'comentarios': {
						'comentario1': {
							'comentarioId': 1,
							'mensaje': 'Mensaje 1'
						},
						'comentario2': {
							'comentarioId': 2,
							'mensaje': 'Mensaje 2'
						}
					}
				}
			}
        return comentarios, 200
    except Exception as e:
        return {'error': str(e)}, 500





if __name__ == '__main__':
    app.run(debug=True)