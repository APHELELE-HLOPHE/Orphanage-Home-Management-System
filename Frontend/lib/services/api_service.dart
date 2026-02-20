import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/children.dart';
import '../models/donation_request.dart';
import '../models/book_visit_request.dart';

class ApiService {
  static const String baseUrl = 'http://localhost:8080/OrphanageHome/api';
  
  static Future<List<Children>> getChildren() async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/children'),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
      );

      if (response.statusCode == 200) {
        final List<dynamic> jsonList = json.decode(response.body);
        final List<Children> children = [];
        for (var json in jsonList) {
          children.add(Children.fromJson(json));
        }
        return children;
      } else {
        throw Exception('Failed to load children. Status: ${response.statusCode}');
      }
    } catch (e) {
      print('Error in getChildren: $e');
      rethrow; 
    }
  }

  static Future<Map<String, dynamic>> makeDonation(DonationRequest request) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/donations'),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: json.encode({
          'itemName': request.itemName,
          'quantity': request.quantity,
          'description': request.description,
        }),
      );

      if (response.statusCode == 201) {
        return {
          'success': true,
          'message': 'Donation submitted successfully!'
        };
      } else {
        return {
          'success': false,
          'message': 'Failed to submit donation. Status: ${response.statusCode}'
        };
      }
    } catch (e) {
      print('Error in makeDonation: $e');
      return {
        'success': false,
        'message': 'Network error: $e'
      };
    }
  }

  static Future<Map<String, dynamic>> bookVisit(BookVisitRequest request) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/bookvisit'),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: json.encode({
          'name': request.name,
          'surname': request.surname,
          'phoneNumber': request.phoneNumber,
          'visitDate': request.visitDate,
        }),
      );

      if (response.statusCode == 201) {
        return {
          'success': true,
          'message': 'Visit booked successfully!'
        };
      } else {
        return {
          'success': false,
          'message': 'Failed to book visit. Status: ${response.statusCode}'
        };
      }
    } catch (e) {
      print('Error in bookVisit: $e');
      return {
        'success': false,
        'message': 'Network error: $e'
      };
    }
  }
}
