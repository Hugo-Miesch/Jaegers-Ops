//
//  Ticket.swift
//  Jeager-ops-Ipad2
//

import Foundation

struct Ticket: Decodable {
    let id: Int
    let issue: String
    let priority: String
    let status: String
    let created_at: String
    let resolved_at: String?
    let jaeger_name: String
    let entrepot_name: String
}

