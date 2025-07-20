//
//  Order.swift
//  Jeager-ops-Ipad2
//
//  Created by Luca Guilliere on 20/07/2025.
//

struct Order: Codable {
    let id: Int
    let ticket_id: Int
    let technicien_id: Int
    let status: String
    let created_at: String
    let ticket_issue: String
    let jaeger_name: String
    let pieces: [String]
}
