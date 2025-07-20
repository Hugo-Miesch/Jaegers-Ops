//
//  AuthService.swift
//  iOrganShop
//
//  Created by Luca Guilliere on 12/05/2025.
//
import Foundation

struct Zoo: Codable {
    let _id: String
    let name: String
}

class AuthService {
    
    static func login(body: LoginBody, completion: @escaping (String?, Error?) -> Void) {
        guard let url = URL(string: "\(HttpEnv.host)/tech/login") else {
            print("[AuthService] URL invalide")
            completion(nil, NSError(domain: "com.luca.invalid_url", code: 3))
            return
        }
        
        var req = URLRequest(url: url)
        req.httpMethod = "POST"
        
        let bodyDict = body.toDictionnary()
        let json = try? JSONSerialization.data(withJSONObject: bodyDict)
        req.httpBody = json
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let task = URLSession.shared.dataTask(with: req) { data, res, err in
            if let err = err {
                print("[AuthService] Erreur réseau :", err.localizedDescription)
                completion(nil, err)
                return
            }
            
            guard let httpResponse = res as? HTTPURLResponse else {
                print("[AuthService] Réponse HTTP invalide")
                completion(nil, NSError(domain: "com.luca.invalid_response", code: 4))
                return
            }
            
            print("[AuthService] Code HTTP :", httpResponse.statusCode)
            
            if httpResponse.statusCode == 200 {
                print("[AuthService] Connexion acceptée.")
                completion("OK", nil) // pas de vraie session mais on indique que ça a marché
            } else {
                print("[AuthService] Erreur API, code:", httpResponse.statusCode)
                completion(nil, NSError(domain: "com.luca.invalid_credentials", code: httpResponse.statusCode))
            }
        }
        task.resume()
    }
}
