class Children {
  final int id;
  final String name;
  final String surname;
  final int age;
  final String description;

  Children({
    required this.id,
    required this.name,
    required this.surname,
    required this.age,
    required this.description,
  });

  factory Children.fromJson(Map<String, dynamic> json) {
    return Children(
      id: json['id'] ?? 0,
      name: json['name']?.toString() ?? '',
      surname: json['surname']?.toString() ?? '',
      age: json['age'] ?? 0,
      description: json['description']?.toString() ?? '',
    );
  }

  Map<String, dynamic> toJson() => {
    'id': id,
    'name': name,
    'surname': surname,
    'age': age,
    'description': description,
  };
}
