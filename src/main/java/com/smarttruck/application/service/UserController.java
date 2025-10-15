import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

@PutMapping("/{id}")
public ResponseEntity<CreateUserResponse> updateUser(
    @PathVariable String id,
    @RequestBody CreateUserRequest request) {

    try {
        User updatedUser = userService.updateUser(id, request.getName(), request.getEmail(),
            request.getPassword(), request.getPhone());
        return ResponseEntity.ok(UserMapper.toResponse(updatedUser));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(null);
    }
}


@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    try {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204
    } catch (IllegalArgumentException e) {
        return ResponseEntity.notFound().build(); // 404
    }
}
