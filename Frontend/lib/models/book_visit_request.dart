class BookVisitRequest {
  final String name;
  final String surname;
  final String phoneNumber;
  final String visitDate;

  BookVisitRequest({
    required this.name,
    required this.surname,
    required this.phoneNumber,
    required this.visitDate,
  });

  Map<String, dynamic> toJson() {
    return {
      'name': name,
      'surname': surname,
      'phoneNumber': phoneNumber,
      'visitDate': visitDate,
    };
  }
}